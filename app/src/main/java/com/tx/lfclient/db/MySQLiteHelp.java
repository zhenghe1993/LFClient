package com.tx.lfclient.db;

import org.xutils.DbManager;
import org.xutils.db.table.TableEntity;
import org.xutils.x;

/**
 * Created by (IMP)郑和明
 * Date is 2017/2/9
 */
public class MySQLiteHelp {

    private static DbManager db;

    static {

        DbManager.DaoConfig config = new DbManager.DaoConfig()
                .setDbName("LF.db")
                .setAllowTransaction(true)
                .setDbVersion(1)
                .setTableCreateListener(new DbManager.TableCreateListener() {
                    @Override
                    public void onTableCreated(DbManager db, TableEntity<?> table) {

                    }
                })
                .setDbUpgradeListener(new DbManager.DbUpgradeListener() {
                    @Override
                    public void onUpgrade(DbManager db, int oldVersion, int newVersion) {

                    }
                });
        db = x.getDb(config);
    }

    public static DbManager getDbManager() {
        return db;
    }

}
