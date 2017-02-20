package com.example.yls.qqdemo.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.yls.qqdemo.app.Constant;

import org.greenrobot.greendao.query.QueryBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 雪无痕 on 2017/1/3.
 */

public class DatabaseManager {
    public static DatabaseManager sDatabaseManager;
    private DaoSession mDaoSession;

    private DatabaseManager(){}
    public static DatabaseManager getInstance(){
        if(sDatabaseManager==null){
            synchronized (DatabaseManager.class){
                if(sDatabaseManager==null){
                    sDatabaseManager=new DatabaseManager();
                }
            }
        }
        return sDatabaseManager;
    }
    public void initDatabase(Context context) {
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context, Constant.Database.DATABASE_NAME, null);
        SQLiteDatabase writableDatabase = devOpenHelper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(writableDatabase);
        mDaoSession = daoMaster.newSession();
    }
    //保存联系人
    public void saveContact(Contact contact){
        ContactDao contactDao = mDaoSession.getContactDao();
        contactDao.save(contact);
    }

    public List<String> queryContact() {
        List<String> contacts=new ArrayList<String>();
        ContactDao contactDao = mDaoSession.getContactDao();
        QueryBuilder<Contact> contactQueryBuilder = contactDao.queryBuilder();
        List<Contact> list = contactQueryBuilder.list();

        for (int i = 0; i <list.size() ; i++) {
            contacts.add(list.get(i).getUserName());
        }
        
        return contacts;
    }
    public  void  deleteContacts(){
        ContactDao contactDao = mDaoSession.getContactDao();
        contactDao.deleteAll();
    }
}
