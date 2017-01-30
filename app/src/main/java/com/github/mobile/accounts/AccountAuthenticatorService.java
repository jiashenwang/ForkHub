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

import static android.accounts.AccountManager.ACTION_AUTHENTICATOR_INTENT;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Authenticator service that returns a subclass of AbstractAccountAuthenticator
 * in onBind()
 */
public class AccountAuthenticatorService extends Service {

    private static AccountAuthenticator AUTHENTICATOR;

    public IBinder onBind(Intent intent) {
        if(AUTHENTICATOR == null) {
            setAuthenticator();
        }

        return intent.getAction().equals(ACTION_AUTHENTICATOR_INTENT) ? AUTHENTICATOR
                .getIBinder() : null;
    }

    private void setAuthenticator() {
        // require: AUTHENTICATOR is null
        // ensure: AUTHENTICATOR = new AccountAuthenticator(this);
        AUTHENTICATOR = new AccountAuthenticator(this);
    }
}