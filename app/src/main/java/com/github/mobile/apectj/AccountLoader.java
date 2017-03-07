package com.github.mobile.apectj;

import android.accounts.Account;
import android.content.Context;

import com.github.mobile.accounts.AuthenticatedUserTask;
import com.github.mobile.persistence.AccountDataManager;
import com.google.inject.Inject;

import org.eclipse.egit.github.core.User;

import java.util.List;


public class AccountLoader extends
        AuthenticatedUserTask<List<User>> {

    @Inject
    private AccountDataManager cache;

    protected AccountLoader(Context context) {
        super(context);
    }

    @Override
    protected List<User> run(Account account) throws Exception {
        return cache.getOrgs(true);
    }
}