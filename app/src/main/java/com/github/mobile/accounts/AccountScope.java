/*
 * Copyright 2012 GitHub Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mobile.accounts;

import android.accounts.Account;
import android.accounts.AccountManager;

import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.OutOfScopeException;
import com.google.inject.Provider;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Custom scope that makes an authenticated GitHub account available by
 * enforcing that the user is logged in before proceeding.
 */
public class AccountScope implements ScopeBase {

    private static final Key<GitHubAccount> GITHUB_ACCOUNT_KEY = Key
            .get(GitHubAccount.class);


    //Added  -------------------------------

    private static final Provider<Object> SEEDED_KEY_PROVIDER = new Provider<Object>() {
        public Object get() {
            throw new IllegalStateException("Object not seeded in this scope");
        }
    };

    /**
     * Returns a provider that always throws an exception complaining that the
     * object in question must be seeded before it can be injected.
     *
     * @return typed provider
     */
    @SuppressWarnings({ "unchecked" })
    public static <T> Provider<T> seededKeyProvider() {
        return (Provider<T>) SEEDED_KEY_PROVIDER;
    }

    public <T> Provider<T> scope(final Key<T> key, final Provider<T> unscoped) {
        return new Provider<T>() {
            public T get() {
                Map<Key<?>, Object> scopedObjects = getScopedObjectMap(key);

                @SuppressWarnings("unchecked")
                T current = (T) scopedObjects.get(key);
                if (current == null && !scopedObjects.containsKey(key)) {
                    current = unscoped.get();
                    scopedObjects.put(key, current);
                }
                return current;
            }
        };
    }


    // Added ----------------------------------

    /**
     * Create new module
     *
     * @return module
     */
    public static Module module() {
        return new AbstractModule() {
            public void configure() {
                AccountScope scope = new AccountScope();

                bind(AccountScope.class).toInstance(scope);

                bind(GITHUB_ACCOUNT_KEY).toProvider(
                        AccountScope.<GitHubAccount> seededKeyProvider()).in(
                        scope);
            }
        };
    }

    private final ThreadLocal<GitHubAccount> currentAccount = new ThreadLocal<GitHubAccount>();

    private final Map<GitHubAccount, Map<Key<?>, Object>> repoScopeMaps = new ConcurrentHashMap<GitHubAccount, Map<Key<?>, Object>>();

    /**
     * Enters scope using a GitHubAccount derived from the supplied account
     *
     * @param account
     * @param accountManager
     */
    public void enterWith(final Account account,
            final AccountManager accountManager) {
        enterWith(new GitHubAccount(account, accountManager));
    }

    /**
     * Enter scope with account
     *
     * @param account
     */
    public void enterWith(final GitHubAccount account) {
        if (currentAccount.get() != null)
            throw new IllegalStateException(
                    "A scoping block is already in progress");

        currentAccount.set(account);
    }

    /**
     * Exit scope
     */
    public void exit() {
        if (currentAccount.get() == null)
            throw new IllegalStateException("No scoping block in progress");

        currentAccount.remove();
    }

    @Override
    public <T> Map<Key<?>, Object> getScopedObjectMap(final Key<T> key) {
        GitHubAccount account = currentAccount.get();
        if (account == null)
            throw new OutOfScopeException("Cannot access " + key
                    + " outside of a scoping block");

        Map<Key<?>, Object> scopeMap = repoScopeMaps.get(account);
        if (scopeMap == null) {
            scopeMap = new ConcurrentHashMap<Key<?>, Object>();
            scopeMap.put(GITHUB_ACCOUNT_KEY, account);
            repoScopeMaps.put(account, scopeMap);
        }
        return scopeMap;
    }
}
