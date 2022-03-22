package com.me.support.datacache.info;

import org.litepal.crud.DataSupport;

import java.util.List;

public class DbInfo {
    /**
     * litepal的使用步骤
     * 1、在业务层定义一个类 UserBean extend DataSupport{}
     * 2、在业务层的main目录下添加一个assets文件夹，在assets里面添加一个litepal.xml文件
     * 3、litepal.xml文件里面添加以下的内容
     * <?xml version="1.0" encoding="utf-8"?>
     * <litepal>
     *
     *     <dbname value="choulidb">  //数据库名称
     *     </dbname>
     *     <version value="1">  //版本号，下面的类发生改变时，要加 1
     *     </version>
     *     <list>
     *         <mapping class="com.example.exercisendk.UserBean">  //自定义的类名，需要 extend DataSupport
     *         </mapping>
     *     </list>
     *
     * </litepal>
     * //更详细的使用方法 ：https://github.com/LitePalFramework/LitePal
     */

    private static DbInfo mDbInfo = new DbInfo();

    public static DbInfo getInstance() {
        return mDbInfo;
    }

    /**
     * 添加数据
     * UserBean userBean = new UserBean();
     * userBean.setName("张一");
     * userBean.setAge(22);
     * AppDataCache.withDb().addDbInfo(userBean);
     * @param type
     * @param <T>
     */
    public <T extends DataSupport> void addDbInfo(T type) {
        type.save();
    }

    /**
     * 根据id进行更新
     * UserBean userBean1 = new UserBean();
     * userBean1.setName("张二");
     * userBean1.setAge(23);
     * AppDataCache.withDb().updataInfo(userBean1,1);
     * @param type userBean1
     * @param id 1
     * @param <T>
     */
    public <T extends DataSupport> void updataInfo(T type, long id) {
        type.update(id);
    }

    /**
     * 根据条件进行更新
     * AppDataCache.withDb().updataAllInfo(userBean1,"age > ?","22");
     * AppDataCache.withDb().updataAllInfo(userBean1,"age > ? and name = ?","22","张三");
     * @param type
     * @param conditions
     * @param <T>
     */
    public <T extends DataSupport> void updataAllInfo(T type, String... conditions) {
        type.updateAll(conditions);
    }

    /**
     * 根据id删除
     * AppDataCache.withDb().removeAllInfo(UserBean.class,1);
     * @param clazz UserBean.class
     * @param id 1，默认id是从1开始的
     * @param <T>
     */
    public <T extends DataSupport> void removeInfo(Class<T> clazz, long id) {
        DataSupport.delete(clazz, id);
    }

    /**
     * 根据conditions条件删除数据，如果不传conditions即删除全部
     * AppDataCache.withDb().removeAllInfo(UserBean.class,"age > ?","22");
     * AppDataCache.withDb().removeAllInfo(UserBean.class,"age > ? and name = ?","22","张三");
     * @param clazz UserBean.class
     * @param conditions "age > ?","22" 或 "age > ? and name = ?","22","张三"
     * @param <T>
     */
    public <T extends DataSupport> void removeAllInfo(Class<T> clazz, String... conditions) {
        DataSupport.deleteAll(clazz, conditions);
    }

    /**
     * 不传ids时，即查全部,只能根据Id进行查找，没办法按条件进行查找，这个是litepal库不好的地方
     * List<UserBean> userBeans = AppDataCache.withDb().getAllInfo(UserBean.class);
     * @param clazz UserBean.class
     * @param ids
     * @param <T>
     * @return
     */
    public <T extends DataSupport> List<T> getAllInfo(Class<T> clazz, long... ids) {
        return DataSupport.findAll(clazz,ids);
    }

    /**
     * 根据id进行查找
     * UserBean userBean = AppDataCache.withDb().getAllInfo(UserBean.class,1);
     * @param clazz UserBean
     * @param id 1
     * @param <T>
     * @return
     */
    public <T extends DataSupport> T getInfo(Class<T> clazz, long id) {
        return DataSupport.find(clazz, id);
    }

    /**
     * 查找数据库中第一条数据
     * AppDataCache.withDb().getFirstInfo(UserBean.class);
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends DataSupport> T getFirstInfo(Class<T> clazz) {
        return DataSupport.findFirst(clazz);
    }

    /**
     * 查找数据库中最后一条数据
     * AppDataCache.withDb().getLastInfo(UserBean.class);
     * @param clazz
     * @param <T>
     * @return
     */
    public <T extends DataSupport> T getLastInfo(Class<T> clazz) {
        return DataSupport.findLast(clazz);
    }
}
