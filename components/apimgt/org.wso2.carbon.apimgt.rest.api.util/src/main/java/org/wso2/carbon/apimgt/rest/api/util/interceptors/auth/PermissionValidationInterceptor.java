/*
 *
 *  Copyright (c) 2022, WSO2 LLC. (http://www.wso2.com) All Rights Reserved.
 *
 *  WSO2 LLC. licenses this file to you under the Apache License,
 *  Version 2.0 (the "License"); you may not use this file except
 *  n compliance with the License.
 *  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 *
 */

package org.wso2.carbon.apimgt.rest.api.util.interceptors.auth;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.cxf.configuration.security.AuthorizationPolicy;
import org.apache.cxf.interceptor.Fault;
import org.apache.cxf.interceptor.security.AuthenticationException;
import org.apache.cxf.message.Message;
import org.apache.cxf.phase.AbstractPhaseInterceptor;
import org.apache.cxf.phase.Phase;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;

/**
 * This interceptor checks user permission
 */
public class PermissionValidationInterceptor extends AbstractPhaseInterceptor {

    private String permission;
    private AuthorizationPolicy authorizationPolicy;
    private static final Log log = LogFactory.getLog(PermissionValidationInterceptor.class);

    public PermissionValidationInterceptor() {
        // We will use PRE_INVOKE phase as we need to process message before hit actual
        // service
        super(Phase.PRE_INVOKE);
    }

    @Override
    public void handleMessage(Message message) throws Fault {
        AuthorizationPolicy policy = message.get(AuthorizationPolicy.class);
        String username = policy.getUserName();
        try {
            if (log.isDebugEnabled()) {
                log.debug("Attempting to validate permission " + permission + " for user: " + username);
            }
            if (APIUtil.hasPermission(username, permission)) {
                log.debug("Permission is granted");
            } else {
                throw new AuthenticationException("Unauthenticated request");
            }
        } catch (APIManagementException e) {
            log.error("Error while checking the user permission", e);
            throw new AuthenticationException("Unauthenticated request");
        }
    }

    public String getPermission() {

        return permission;
    }

    public void setPermission(String permission) {

        this.permission = permission;
    }
}
