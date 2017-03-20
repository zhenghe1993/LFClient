package com.tx.lfclient.db;

import com.tx.lfclient.entities.User;

import org.xutils.DbManager;
import org.xutils.common.util.LogUtil;
import org.xutils.ex.DbException;

import java.util.List;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/14
 */
public class UserDbManager {
    private DbManager db;
    private static UserDbManager userDbManager;

    private UserDbManager() {
        db = MySQLiteHelp.getDbManager();
    }

    public static synchronized UserDbManager getInstance() {
        if (userDbManager == null) {
            userDbManager = new UserDbManager();
        }
        return userDbManager;
    }

    //根据id查询
    public User getUserById(Long id) {

        try {
            List<User> list = db.selector(User.class).where("ID", "=", id).findAll();
            LogUtil.i("list=" + list+"id="+id);
            if (list == null || list.size() == 0) {
                return null;
            } else {
                LogUtil.i(list.toString());
                return list.get(0);
            }

        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }


    //根据位置查询
    public List<User> getUserByLocation(String location) {
        try {
            return db.selector(User.class).where("LOCATION", "=", location).findAll();
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

    //添加或更新

    public void saveOrUpdateUser(User user) {
        try {
            User u = getUserById(user.getId());
            if (u == null) {
                LogUtil.i(user.toString());
                db.save(user);
                LogUtil.i(user.toString());
            } else {
                db.saveOrUpdate(user);
            }
        } catch (DbException e) {
            e.printStackTrace();
        }
    }

    public List<User> getUserAll() {
        try {
            return db.findAll(User.class);
        } catch (DbException e) {
            e.printStackTrace();
        }
        return null;
    }

}
