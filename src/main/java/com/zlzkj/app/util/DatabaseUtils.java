package com.zlzkj.app.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.*;

/**
 * Class description
 *
 * @author wxf
 * @version 1.0.0, 18/05/8
 */
public class DatabaseUtils {

    private static String dbUrl1 = "jdbc:mysql://localhost:3306/smartfire?characterEncoding=utf8&useSSL=false";
    //用户名
    private static String dbUserName = "root";
    //密码
    private static String dbPassword = "root";
    //驱动名称
    private static String jdbcName = "com.mysql.jdbc.Driver";

    public static void main(String[] args) {
        List<Map> mapList = new ArrayList<>();
        DecimalFormat df = new DecimalFormat("#.00");
        for(int i=1;i<23;i++){
            Map paraMap = new HashMap();
            /*paraMap.put("equipment_id",i%7+1);
            paraMap.put("category",Math.random()>0.5?"1":"0");
            paraMap.put("level",Math.random()>0.5?"1":"0");
            paraMap.put("status",Math.random()>0.5?"1":"0");
            paraMap.put("region_id",16);
            paraMap.put("tenant_id",1);*/
            /*paraMap.put("voltage_a",df.format(220.3 *  Math.random()));
            paraMap.put("voltage_b",df.format(30 * Math.random()));
            paraMap.put("voltage_c",df.format(40 * Math.random()));
            paraMap.put("voltage_n",df.format(50 * Math.random()));

            paraMap.put("electric_current_a",df.format( 20*Math.random()));
            paraMap.put("electric_current_b",df.format( 20*Math.random()));
            paraMap.put("electric_current_c",df.format( 20*Math.random()));
            paraMap.put("electric_current_n",df.format( 20*Math.random()));

            paraMap.put("temperature_a",df.format(90 *  Math.random()));
            paraMap.put("temperature_b",df.format(90 * Math.random()));
            paraMap.put("temperature_c",df.format(90 * Math.random()));
            paraMap.put("temperature_n",df.format(90 * Math.random()));
*/
            paraMap.put("created_time","2017-12-22 "+(((int)(Math.random()*24)< 10?"0"+(int)(Math.random()*24):(int)(Math.random()*24)))+":"+(int)(Math.random()*60)+":"+(int)(Math.random()*60));

            paraMap.put("type",1);
            paraMap.put("description","设备螺丝松动，易引发火灾事故，请及时处理");
            paraMap.put("level",0);
            paraMap.put("region_id",9);
            paraMap.put("creator_id",1);
            paraMap.put("tenant_id",1);
            Map whereMap = new HashMap();
            //whereMap.put("created_time","2018-06-17 17:17:17");
            paraMap.put("created_time","2018-"+((int)(Math.random()*12%5)+1)+"-22 "+(((int)(Math.random()*24)< 10?"0"+(int)(Math.random()*24):(int)(Math.random()*24)))+":"+(int)(Math.random()*60)+":"+(int)(Math.random()*60));

            //paraMap.put("where",whereMap);
            mapList.add(paraMap);
        }

        new DatabaseUtils().insert(mapList,"x_precursor");
        //new DatabaseUtils().update(mapList,"x_electricity_data");
    }

    public int insert(List<Map> mapList, String tableName) {

        try {
            Class.forName(jdbcName);
            System.out.println("加载驱动成功！");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch blocke.printStackTrace();
            System.out.println("加载驱动失败！");
        }

        Connection conn = null;
        Statement stmt = null;
        int count = 0;
        try {
            //获取数据库连接
            conn = DriverManager.getConnection(dbUrl1, dbUserName, dbPassword);
            System.out.println("数据库连接成功！");
            stmt = conn.createStatement();

            for(Map map :mapList){

                String keys = StringUtils.parentheses(join(new ArrayList<String>(map.keySet())));

                String values = StringUtils.parentheses(join$(new ArrayList<String>(map.values())));

                String sql = "insert into "+tableName+" "+keys+" values"+values;

                System.out.println(sql);

                count += stmt.executeUpdate(sql);
            }

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

        return count;
    }

    public int update(List<Map> mapList, String tableName) {

        try {
            Class.forName(jdbcName);
            System.out.println("加载驱动成功！");
        } catch (ClassNotFoundException e) {
            // TODO Auto-generated catch blocke.printStackTrace();
            System.out.println("加载驱动失败！");
        }

        Connection conn = null;
        Statement stmt = null;
        int count = 0;
        try {
            //获取数据库连接
            conn = DriverManager.getConnection(dbUrl1, dbUserName, dbPassword);
            System.out.println("数据库连接成功！");
            stmt = conn.createStatement();

            for(Map map :mapList){

                String sets = "";

                Map whereMap = (HashMap)map.get("where");
                String where = "";
                for(String key :new ArrayList<String>(whereMap.keySet())){
                    where += key + "='"+ whereMap.get(key) + "' and ";
                }

                where = StringUtils.delLastChar(where ," and ");

                map.remove("where");
                for(String key:new ArrayList<String>(map.keySet())){
                    sets += key + "='"+map.get(key)+"',";
                }
                sets = StringUtils.delLastChar(sets);



                String sql = "update " + tableName + " set "+sets+" where " + where;

                System.out.println(sql);

                count += stmt.executeUpdate(sql);
            }

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

        return count;
    }


    public String join(String [] strs){
        StringBuilder stringBuilder = new StringBuilder();
        if(strs.length == 0){
            return "";
        }
        stringBuilder.append(strs[0]);
        for(int i=1;i<strs.length;i++){
            stringBuilder.append(",").append(strs[i]);
        }
        return stringBuilder.toString();

    }

    public String join(List<String> strs){
        StringBuilder stringBuilder = new StringBuilder();
        if(strs.size() == 0){
            return "";
        }
        stringBuilder.append(""+strs.get(0));
        for(int i=1;i<strs.size();i++){
            stringBuilder.append(",").append(strs.get(i));
        }
        return stringBuilder.toString();

    }

    public String join$(List<String> strs){
        StringBuilder stringBuilder = new StringBuilder();
        if(strs.size() == 0){
            return "";
        }

        Object o = strs.get(0);
        if(o instanceof Integer){
            stringBuilder.append("'").append(String.valueOf(o)).append("'");
        }else if(o instanceof String){
            stringBuilder.append("'").append(o).append("'");
        }

        //stringBuilder.append("'").append(""+strs.get(0)).append("'");
        for(int i=1;i<strs.size();i++){
            o = strs.get(i);
            if(o instanceof Integer){
                stringBuilder.append(",'").append(String.valueOf(o)).append("'");
            }else if(o instanceof String){
                stringBuilder.append(",'").append(o).append("'");
            }
        }
        return stringBuilder.toString();

    }

    public String join(Set<String> strs){
        StringBuilder stringBuilder = new StringBuilder();
        if(strs.size() == 0){
            return "";
        }
        stringBuilder.append(strs.iterator());
        for(int i=1;i<strs.size();i++){
            stringBuilder.append(",").append(strs.iterator());
        }
        return stringBuilder.toString();

    }
}


//~ Formatted by Jindent --- http://www.jindent.com
