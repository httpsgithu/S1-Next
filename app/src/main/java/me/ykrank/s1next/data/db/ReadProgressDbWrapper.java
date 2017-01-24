package me.ykrank.s1next.data.db;

import android.support.annotation.NonNull;

import javax.inject.Inject;

import me.ykrank.s1next.App;
import me.ykrank.s1next.data.db.dbmodel.ReadProgress;
import me.ykrank.s1next.data.db.dbmodel.ReadProgressDao;

import static me.ykrank.s1next.data.db.dbmodel.ReadProgressDao.Properties;

/**
 * 对黑名单数据库的操作包装
 * Created by AdminYkrank on 2016/2/23.
 */
public class ReadProgressDbWrapper {
    private static ReadProgressDbWrapper dbWrapper = new ReadProgressDbWrapper();

    @Inject
    AppDaoSessionManager appDaoSessionManager;

    private ReadProgressDbWrapper() {
        App.getAppComponent().inject(this);
    }

    public static ReadProgressDbWrapper getInstance() {
        return dbWrapper;
    }

    private ReadProgressDao getReadProgressDao() {
        return appDaoSessionManager.getDaoSession().getReadProgressDao();
    }

    public ReadProgress getWithThreadId(String threadId) {
        return getReadProgressDao().queryBuilder()
                .where(Properties.ThreadId.eq(threadId))
                .unique();
    }

    public void saveReadProgress(@NonNull ReadProgress readProgress) {
        ReadProgress oReadProgress = getWithThreadId(readProgress.getThreadId());
        if (oReadProgress == null) {
            getReadProgressDao().insert(readProgress);
        } else {
            oReadProgress.copyFrom(readProgress);
            getReadProgressDao().update(oReadProgress);
        }
    }

    public void delReadProgress(String threadId) {
        ReadProgress oReadProgress = getWithThreadId(threadId);
        if (oReadProgress != null) {
            getReadProgressDao().delete(oReadProgress);
        }
    }

}
