package com.zlzkj.app.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Class description
 *
 * @author wxf
 * @version 1.0.0, 18/05/8
 */
public class MapperUtils {

    private static HashMap<String, String> JDBC_TYPE = new HashMap<String, String>() {{
        put("char", "CHAR");
        put("varchar", "VARCHAR");
        put("int", "INTEGER");
        put("timestamp", "TIMESTAMP");
        put("tinyint", "TINYINT");
        put("text", "LONGVARCHAR");
        put("double", "DOUBLE");
    }};

    private static HashMap<String, String> JAVA_TYPE = new HashMap<String, String>() {{
        put("char", "String");
        put("varchar", "String");
        put("int", "Integer");
        put("timestamp", "Date");
        put("tinyint", "byte");
        put("text", "String");
        put("double", "double");
    }};

    private static String dbUrl1 = "jdbc:mysql://localhost:3306/smartfire?characterEncoding=utf8&useSSL=false";
    //用户名
    private static String dbUserName = "root";
    //密码
    private static String dbPassword = "root";
    //驱动名称
    private static String jdbcName = "com.mysql.jdbc.Driver";

    public static void main(String[] args) {

        new MapperUtils().createMapper("index", "test", "x_equipment_region");

        new MapperUtils().createService("index", "test");
        //searchFile("form.java");
    }

    //例：     com/zlzkj/app/mapper/impl/UserMapper.java  com/zlzkj/app/mapper/impl/UserMapper.xml   com/zlzkj/app/model/impl/User.java
    //folder: mapper下的文件夹名称 如：impl
    //module: 数据类名称 如：user
    public void createMapper(String folder, String module, String tableName) {


        try {
            File directory = new File("");//参数为空
            String courseFile = directory.getCanonicalPath();
            String mapperPath = courseFile + "/src/main/java/com/zlzkj/app/mapper/";
            String modelPath = courseFile + "/src/main/java/com/zlzkj/app/model/";

            //String folder = "example";
            //String module = "example";
            String mapperName = firstUpperCase(module) + "Mapper.xml";
            String daoName = firstUpperCase(module) + "Mapper.java";
            String modelName = firstUpperCase(module) + ".java";


            String targetMapperPath = mapperPath + "/" + folder + "/" + mapperName;
            String targetDaoPath = mapperPath + "/" + folder + "/" + daoName;
            String targetModelPath = modelPath + "/" + folder + "/" + modelName;

            if (new File(targetMapperPath).exists()) {
                System.out.println("======>>>>>>> " + mapperName + " file has already existed ! please delete first and try again!  ");
                return;
            }

            if (new File(targetDaoPath).exists()) {
                System.out.println("======>>>>>>> " + daoName + " file has already existed ! please delete first and try again!  ");
                return;
            }

            if (new File(targetModelPath).exists()) {
                System.out.println("======>>>>>>> " + modelName + " file already existed ! please delete first and try again!  ");
                return;
            }
            //获取字段信息
            List<Field> fields = getFields(module, tableName);
            //生成xml文件
            write(targetMapperPath, generateXml(folder, module, fields, tableName));
            System.out.println("======>>>>>>>  xml file generated successfully !!");

            //生成dao .java层文件
            write(targetDaoPath, generateDao(folder, module, fields));
            System.out.println("======>>>>>>>  java file generated successfully !!");

            //生成model层文件
            write(targetModelPath, generateModel(folder, module, fields));
            System.out.println("======>>>>>>>  model file generated successfully !!");

            //System.out.println("======>>>>>>>  operation succeeds !!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Field> getFields(String module, String tableName) {

        try {
            Class.forName(jdbcName);
            System.out.println("加载驱动成功！");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch blocke.printStackTrace();
            System.out.println("加载驱动失败！");
        }

        Connection conn = null;
        Statement stmt = null;
        List<Field> list = new ArrayList<>();
        try {
            //获取数据库连接
            conn = DriverManager.getConnection(dbUrl1, dbUserName, dbPassword);
            System.out.println("数据库连接成功！");
            //System.out.println("进行数据库操作！");//执行查询

            String sql = "select t.column_comment,t.column_name,t.data_type from information_schema.columns t where TABLE_SCHEMA='smartfire' and TABLE_NAME= '" + tableName + "';";
            stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            //从查询结果中取出需要的信息
            while (rs.next()) {

                list.add(new Field(rs.getString("column_comment"), rs.getString("column_name"), rs.getString("data_type")));

            }

            //关闭数据库连接释放资源
            rs.close();
            stmt.close();
            conn.close();

        } catch (SQLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("获取数据库连接失败！");
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        if (list.size() == 0) {
            System.out.println("数据表中无字段！！！！");
            return new ArrayList<>();
        }
        return list;

    }

    public String generateModel(String folder, String module, List<Field> list) {

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package com.zlzkj.app.model.").append(folder).append(";\n\n");
        stringBuilder.append("import org.springframework.format.annotation.DateTimeFormat;\n");
        stringBuilder.append("import java.util.Date;\n\n");
        stringBuilder.append("public class ").append(firstUpperCase(module)).append("{\n\n");

        for (Field field : list) {
            //jdbcType 转成 java中的格式
            String dataType = JAVA_TYPE.get(field.getFieldType());
            String fieldName = field.getFieldName();

            //  获取字段注释
            String fieldMemo = field.getFieldMemo();

            if (fieldMemo != null && !fieldMemo.equals("")) {
                stringBuilder.append("    //").append(fieldMemo).append("\n");
            }
            //System.out.println(fieldMemo);

            if (dataType.equals("Date")) {
                stringBuilder.append("    @DateTimeFormat(pattern = \"yyyy-MM-dd\")\n");
            }
            stringBuilder.append("    private ").append(dataType).append(" ").append(format(fieldName)).append(";\n\n");
        }

        for (Field field : list) {
            //jdbcType 转成 java中的格式
            String dataType = JAVA_TYPE.get(field.getFieldType());
            String fieldName = field.getFieldName();
            String formatFieldName = format(fieldName);

            stringBuilder.append("    public ").append(dataType).append(" get").append(firstUpperCase(formatFieldName)).append("() {\n");
            stringBuilder.append("        return ").append(formatFieldName).append(";\n");
            stringBuilder.append("    } \n\n");

            stringBuilder.append("    public void ").append("set").append(firstUpperCase(formatFieldName)).append("(").append(dataType).append(" ").append(formatFieldName).append(") {\n");
            stringBuilder.append("        this.").append(formatFieldName).append(" = ").append(formatFieldName).append(";\n");
            stringBuilder.append("    } \n\n");
        }
        stringBuilder.append("  }");

        return stringBuilder.toString();
    }

    public String generateDao(String folder, String module, List<Field> list) {

        String firstUpperCasedModule = firstUpperCase(module);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("package com.zlzkj.app.mapper.").append(folder).append(";\n")
                .append("import com.zlzkj.app.model.").append(folder).append(".").append(firstUpperCasedModule).append(";\n\n")
                .append("import com.zlzkj.app.model.Row;\n")
                .append("import java.util.List;\n")
                .append("import java.util.Map;\n\n");
        stringBuilder.append("public interface ").append(firstUpperCasedModule).append("Mapper {\n");
        stringBuilder.append("  int deleteByPrimaryKey(int id);\n\n");

        stringBuilder.append("  int deleteByMap(Map<String, Object> map);\n\n");

        stringBuilder.append("  int insert(").append(firstUpperCasedModule).append(" record);\n\n");

        stringBuilder.append("  ").append(firstUpperCasedModule).append(" selectByPrimaryKey(Integer id);\n\n");

        stringBuilder.append("  List<Row> selectByMap(Map<String, Object> map);\n\n");

        stringBuilder.append("  int countByMap(Map<String, Object> map);\n\n");

        stringBuilder.append("  int updateByPrimaryKey(").append(firstUpperCasedModule).append(" record);\n\n");

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    public String generateXml(String folder, String module, List<Field> list, String tableName) {
        //String folder = "example";
        //String module = "example";
        //String tableName = "x_"+module;


        String firstUpperCasedModule = firstUpperCase(module);

        StringBuilder stringBuilder = new StringBuilder();


        //============>>>>>>> resultMap start
        stringBuilder.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\n")
                .append("<!DOCTYPE mapper PUBLIC \"-//mybatis.org//DTD Mapper 3.0//EN\" \"http://mybatis.org/dtd/mybatis-3-mapper.dtd\" >\n")
                .append("<mapper namespace=\"com.zlzkj.app.mapper.").append(folder).append(".").append(firstUpperCasedModule).append("Mapper\" >\n")
                .append("<resultMap id=\"BaseResultMap\" type=\"com.zlzkj.app.model.").append(folder).append(".").append(firstUpperCasedModule).append("\" > \n");

        for (Field field : list) {

            //jdbcType 转成 Mapper中的格式
            String dataType = JDBC_TYPE.get(field.getFieldType());

            // format()  格式化字段名   如：field_name => fieldName
            String fieldName = field.getFieldName();
            if (fieldName.equals("id")) {
                stringBuilder.append("   <id column=\"id\" property=\"id\" jdbcType=\"").append(dataType).append("\" /> \n");
            } else {
                stringBuilder.append("   <result column=\"").append(fieldName).append("\" property=\"").append(format(fieldName)).append("\" jdbcType=\"").append(dataType).append("\" /> \n");
            }
        }
        stringBuilder.append("</resultMap> \n");
        //<<<<<<<<<==========


        //==========>>>>>>> Base_Column_List start
        stringBuilder.append("<sql id=\"Base_Column_List\">\n ");

        stringBuilder.append(list.get(0).getFieldName());

        for (int i = 1; i < list.size(); i++) {
            stringBuilder.append("," + list.get(i).getFieldName());
        }

        stringBuilder.append("\n");
        stringBuilder.append("</sql> \n");
        //<<<<<<<<<==========


        //==========>>>>>>> Base_Column_List start
        stringBuilder.append("<sql id=\"Base_Column_List2\">\n ");

        stringBuilder.append("o.").append(list.get(0).getFieldName());

        for (int i = 1; i < list.size(); i++) {
            stringBuilder.append(",o." + list.get(i).getFieldName());
        }

        stringBuilder.append("\n");
        stringBuilder.append("</sql> \n");
        //<<<<<<<<<==========


        //==========>>>>>>> deleteByPrimaryKey start
        stringBuilder.append("<delete id=\"deleteByPrimaryKey\" parameterType=\"java.lang.Integer\" >\n");

        stringBuilder.append("  delete from ").append(tableName).append(" \n");
        stringBuilder.append("  where id = #{id,jdbcType=INTEGER} \n");
        stringBuilder.append("</delete> \n");

        //<<<<<<<<<==========


        //==========>>>>>>> deleteByMap start
        stringBuilder.append("<delete id=\"deleteByMap\" parameterType=\"map\" >\n");
        stringBuilder.append("  delete from ").append(tableName).append("  \n");
        stringBuilder.append("  <where> \n");
        stringBuilder.append("      and id in \n");
        stringBuilder.append("      <foreach collection=\"ids\" item=\"id\" open=\"(\" close=\")\" separator=\",\"> \n");
        stringBuilder.append("          #{id} \n");
        stringBuilder.append("      </foreach> \n");
        stringBuilder.append("  </where> \n");
        stringBuilder.append("</delete> \n");

        //<<<<<<<<<==========


        //==========>>>>>>> insert start
        stringBuilder.append("<insert id=\"insert\" parameterType=\"com.zlzkj.app.model.").append(folder).append(".").append(firstUpperCasedModule).append("\" >\n");

        stringBuilder.append("  insert into ").append(tableName).append("  \n");
        stringBuilder.append("  <selectKey resultType=\"java.lang.Integer\" keyProperty=\"id\" order=\"AFTER\" > \n");
        stringBuilder.append("      SELECT LAST_INSERT_ID() \n");
        stringBuilder.append("  </selectKey> \n");

        // fields
        stringBuilder.append("  <trim prefix=\"(\" suffix=\")\" suffixOverrides=\",\" > \n");
        for (Field field : list) {
            String fieldName = field.getFieldName();
            stringBuilder.append("    <if test=\"").append(format(fieldName)).append(" != null\">").append(fieldName).append(",</if> \n");
        }
        stringBuilder.append("  </trim> \n");

        // values
        stringBuilder.append("  <trim prefix=\"values (\" suffix=\")\" suffixOverrides=\",\" > \n");
        for (Field field : list) {
            String fieldName = field.getFieldName();
            stringBuilder.append("    <if test=\"").append(format(fieldName)).append(" != null\">#{").append(format(fieldName)).append("},</if> \n");
        }
        stringBuilder.append("  </trim> \n");
        stringBuilder.append("</insert> \n");

        //<<<<<<<<<==========


        //==========>>>>>>> insert start
        stringBuilder.append("<update id=\"updateByPrimaryKey\" parameterType=\"com.zlzkj.app.model.").append(folder).append(".").append(firstUpperCasedModule).append("\" >\n");

        stringBuilder.append("  update ").append(tableName).append("  \n");

        stringBuilder.append("  <set> \n");
        for (Field field : list) {
            String fieldName = field.getFieldName();
            if (fieldName.equals("id")) continue;
            stringBuilder.append("    <if test=\"").append(format(fieldName)).append(" != null\">")
                    .append(fieldName).append(" = #{").append(format(fieldName)).append("},</if> \n");
        }
        stringBuilder.append("  </set> \n");
        stringBuilder.append("  where id = #{id,jdbcType=VARCHAR} \n");
        stringBuilder.append("</update> \n");

        //<<<<<<<<<==========


        //==========>>>>>>> selectByPrimaryKey start
        stringBuilder.append("<select id=\"selectByPrimaryKey\" resultMap=\"BaseResultMap\" parameterType=\"java.lang.Integer\" >\n");

        stringBuilder.append("  select \n");
        stringBuilder.append("  <include refid=\"Base_Column_List\" /> \n");
        stringBuilder.append("   from  \n");
        stringBuilder.append("  ").append(tableName).append(" \n");
        stringBuilder.append("  where id = #{id,jdbcType=VARCHAR} \n");
        stringBuilder.append("</select> \n");

        //<<<<<<<<<==========


        //==========>>>>>>> selectByMap start
        stringBuilder.append("<select id=\"selectByMap\" resultType=\"com.zlzkj.app.model.Row\" parameterType=\"map\" > \n");
        stringBuilder.append("  select \n");
        stringBuilder.append("  <include refid=\"Base_Column_List\" /> \n");
        stringBuilder.append("   from  \n");
        stringBuilder.append("  ").append(tableName).append(" \n");
        stringBuilder.append("  <where> \n");
        stringBuilder.append("   <if test=\"tenantId != null \">and tenant_id = #{tenantId}</if> \n");
        stringBuilder.append("   <if test=\"id != null \">and id = #{id}</if> \n");
        stringBuilder.append("   <if test=\"ids != null \"> \n");
        stringBuilder.append("       and id in \n");
        stringBuilder.append("       <foreach collection=\"ids\" item=\"id\" open=\"(\" close=\")\" separator=\",\"> \n");
        stringBuilder.append("           #{id} \n");
        stringBuilder.append("       </foreach> \n");
        stringBuilder.append("   </if> \n");
        stringBuilder.append("  </where> \n");

        if (contain("created_time", list))
            stringBuilder.append("  order by created_time \n");

        stringBuilder.append("  <if test=\"start != null and start &gt; -1\"> \n");
        stringBuilder.append("      limit #{start} , #{end} \n");
        stringBuilder.append("  </if> \n");
        stringBuilder.append("</select> \n");

        //<<<<<<<<<==========


        //==========>>>>>>> countByMap start
        stringBuilder.append("<select id=\"countByMap\" resultType=\"java.lang.Integer\" parameterType=\"map\" > \n");
        stringBuilder.append("  select \n");
        stringBuilder.append("    count(*) \n");
        stringBuilder.append("  from  \n");
        stringBuilder.append("    ").append(tableName).append(" \n");
        stringBuilder.append("  <where> \n");
        stringBuilder.append("   <if test=\"tenantId != null \">and tenant_id = #{tenantId}</if> \n");
        stringBuilder.append("   <if test=\"id != null \">and id = #{id}</if> \n");
        stringBuilder.append("  </where> \n");
        stringBuilder.append("</select> \n");


        stringBuilder.append("</mapper> \n");
        //<<<<<<<<<==========

        return stringBuilder.toString();
    }

    public void write(String fileName, String content) {
        File file = new File(fileName);
        String str = content;
        byte bt[] = new byte[1024];
        bt = str.getBytes();
        try {
            FileOutputStream in = new FileOutputStream(file);
            try {
                in.write(bt, 0, bt.length);
                in.close();
                // boolean success=true;
                // System.out.println("写入文件成功");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public String format(String str) {
        StringBuilder stringBuilder = new StringBuilder();
        String[] strs = str.split("_");

        if (strs.length == 0) {
            return "";
        }
        stringBuilder.append(strs[0]);

        for (int i = 1; i < strs.length; i++) {
            if (strs[i].length() == 0)
                strs[i] = "";
            else
                strs[i] = firstUpperCase(strs[i]);
            stringBuilder.append(strs[i]);
        }

        return stringBuilder.toString();
    }

    public String firstUpperCase(String str) {
        if (str.length() == 0)
            return "";
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }


    public class Field {
        String fieldMemo;
        String fieldName;
        String fieldType;

        public Field() {

        }

        public Field(String fieldMemo, String fieldName, String fieldType) {
            this.fieldMemo = fieldMemo;
            this.fieldName = fieldName;
            this.fieldType = fieldType;
        }

        public String getFieldName() {
            return fieldName;
        }

        public void setFieldName(String fieldName) {
            this.fieldName = fieldName;
        }

        public String getFieldType() {
            return fieldType;
        }

        public void setFieldType(String fieldType) {
            this.fieldType = fieldType;
        }

        public String getFieldMemo() {
            return fieldMemo;
        }

        public void setFieldMemo(String fieldMemo) {
            this.fieldMemo = fieldMemo;
        }
    }

    public boolean contain(String fieldName, List<Field> fields) {

        for (Field field : fields) {
            if (fieldName.equals(field.getFieldName())) {
                return true;
            }
        }
        return false;
    }


    public String generateService(String folder, String module) {
        String firstUpperCasedModule = firstUpperCase(module);
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("package com.zlzkj.app.service.").append(folder).append(";\n\n")

                .append("import com.zlzkj.app.model.Row;\n")
                .append("import com.zlzkj.app.model.").append(folder).append(".").append(firstUpperCasedModule).append(";\n")
                .append("import com.zlzkj.app.util.Page;\n")


                .append("import java.util.List;\n")
                .append("import java.util.Map;\n")


                .append("public interface ").append(firstUpperCasedModule).append("Service {\n\n")

                .append("   public Integer delete(int id);\n\n")

                .append("   public Integer update(").append(firstUpperCasedModule).append(" entity);\n\n")

                .append("   public Integer save(").append(firstUpperCasedModule).append(" entity);\n\n")

                .append("   public ").append(firstUpperCasedModule).append(" findById(int id);\n\n")

                .append("   public List<Row> findByMap(Map<String,Object> map);\n\n")

                .append("   public Page findByMap(Map<String,Object> parmMap, Integer nowPage);\n\n")

                .append("}\n")
                ;
        return stringBuilder.toString();
    }

    public String generateServiceImpl(String folder, String module) {

        String firstUpperCasedModule = firstUpperCase(module);

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("package com.zlzkj.app.service.").append(folder).append(".impl;\n\n")

                .append("import com.zlzkj.app.mapper.").append(folder).append(".").append(firstUpperCasedModule).append("Mapper;\n")
                .append("import com.zlzkj.app.model.").append(folder).append(".").append(firstUpperCasedModule).append(";\n")
                .append("import com.zlzkj.app.service.").append(folder).append(".").append(firstUpperCasedModule).append("Service;\n\n")


                .append("import com.zlzkj.app.util.Page;\n")
                .append("import com.zlzkj.app.model.Row;\n")
                .append("import org.springframework.beans.factory.annotation.Autowired;\n")
                .append("import org.springframework.beans.factory.annotation.Value;\n")
                .append("import org.springframework.stereotype.Service;\n")
                .append("import org.springframework.transaction.annotation.Transactional;\n\n")

                .append("import java.util.List;\n")
                .append("import java.util.Map;\n\n")


                .append("@Service\n")
                .append("@Transactional\n")

                .append("public class ").append(firstUpperCasedModule).append("ServiceImpl implements ").append(firstUpperCasedModule).append("Service {\n")


                .append("    @Value(\"${page.size}\")\n")
                .append("    private int PAGE_SIZE;\n\n")

                .append("    @Autowired\n")
                .append("    private ").append(firstUpperCasedModule).append("Mapper mapper;\n\n")

                .append("    public Integer delete(int id){\n")
                .append("        try{\n")
                .append("            return mapper.deleteByPrimaryKey(id);\n")
                .append("        }catch (Exception e){\n")
                .append("            throw new RuntimeException(\"删除失败\");\n")
                .append("        }\n")
                .append("    }\n\n")

                .append("    public Integer update(").append(firstUpperCasedModule).append(" entity){\n")
                .append("        return mapper.updateByPrimaryKey(entity);\n")
                .append("    }\n\n")

                .append("    public Integer save(").append(firstUpperCasedModule).append(" entity) {\n")
                .append("        return mapper.insert(entity);\n")
                .append("    }\n\n")


                .append("    public ").append(firstUpperCasedModule).append(" findById(int id){\n")
                .append("        return mapper.selectByPrimaryKey(id);\n")
                .append("    }\n\n")


                .append("    public List<Row> findByMap(Map<String,Object> map){\n")
                .append("        return mapper.selectByMap(map);\n")
                .append("    }\n\n")


                .append("    public Page findByMap(Map<String,Object> parmMap,Integer nowPage){\n")
                .append("        parmMap.put(\"start\",(nowPage-1)*PAGE_SIZE);\n")
                .append("        parmMap.put(\"end\",PAGE_SIZE);\n")
                .append("        return new Page(findByMap(parmMap),mapper.countByMap(parmMap),nowPage,PAGE_SIZE);\n")
                .append("    }\n")
                .append("}\n");


        return stringBuilder.toString();
    }

    public void createService(String folder, String module) {

        File directory = new File("");//参数为空
        try {
            String courseFile = directory.getCanonicalPath();
            String ServicePath = courseFile + "/src/main/java/com/zlzkj/app/service/";

            //String folder = "example";
            String ServiceName = firstUpperCase(module) + "Service.java";
            String implName = firstUpperCase(module) + "ServiceImpl.java";


            String targetServicePath = ServicePath + "/" + folder + "/" + ServiceName;
            String targetImplPath = ServicePath + "/" + folder + "/impl/" + implName;

            if (new File(targetServicePath).exists()) {
                System.out.println("======>>>>>>> " + ServiceName + " file has already existed ! please delete first and try again!  ");
                return;
            }

            if (new File(targetImplPath).exists()) {
                System.out.println("======>>>>>>> " + implName + " file already existed ! please delete first and try again!  ");
                return;
            }

            //生成interface文件
            write(targetServicePath, generateService(folder, module));
            System.out.println("======>>>>>>>  Service file generated successfully !!");

            //生成impl文件
            write(targetImplPath, generateServiceImpl(folder, module));
            System.out.println("======>>>>>>>  ServiceImpl file generated successfully !!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    /*public static void searchFile(String name) {
        File directory = new File("");//参数为空
        try {
            String courseFile = directory.getCanonicalPath();
            String filePath = courseFile + "/src/main/java/com/zlzkj/app/model/form/form.java";
            File file = new File(filePath);
            System.out.println(filePath);
            if (file.exists()) {
                System.out.println("=========>>>>>>>>file is found !");
                FileReader fr = new FileReader(filePath);

                char[] buf = new char[102];

                int num = 0;

                while ((num = fr.read(buf)) != -1) {
                    System.out.print(new String(buf, 0, num));
                }
                fr.close();
            } else {
                System.out.println("=========>>>>>>>>file is not found !");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}


//~ Formatted by Jindent --- http://www.jindent.com
