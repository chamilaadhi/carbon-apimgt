/*
 * Copyright (c) 2021, WSO2 Inc. (http://www.wso2.org) All Rights Reserved.
 *
 * WSO2 Inc. licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.wso2.carbon.apimgt.api.model;

import org.apache.commons.lang3.StringUtils;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.APIConstants;
import org.wso2.carbon.apimgt.api.dto.GatewayVisibilityPermissionConfigurationDTO;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represent an Environment.
 */
public class Environment implements Serializable {

    private static final long serialVersionUID = 1L;
    private String type = APIConstants.GATEWAY_ENV_TYPE_HYBRID;
    private String serverURL;
    private String userName;
    private String password;
    private String apiGatewayEndpoint;
    private String websocketGatewayEndpoint;
    private String webSubGatewayEndpoint;
    private boolean isDefault;

    // New fields added with dynamic environments
    private Integer id;
    private String uuid;
    private String name;
    private String displayName;
    private String description;
    private boolean isReadOnly;
    private List<VHost> vhosts = new ArrayList<>();
    private String provider;
    private String gatewayType;
    private Map<String, String> additionalProperties = new HashMap<>();

    private String[] visibilityRoles;
    private String visibility;

    public Environment() {
    }

    public Environment(Environment environment) {
        this.type = environment.type;
        this.serverURL = environment.serverURL;
        this.userName = environment.userName;
        this.password = environment.password;
        this.apiGatewayEndpoint = environment.apiGatewayEndpoint;
        this.websocketGatewayEndpoint = environment.websocketGatewayEndpoint;
        this.webSubGatewayEndpoint = environment.webSubGatewayEndpoint;
        this.isDefault = environment.isDefault;
        this.id = environment.id;
        this.uuid = environment.uuid;
        this.name = environment.name;
        this.displayName = environment.displayName;
        this.description = environment.description;
        this.isReadOnly = environment.isReadOnly;
        this.vhosts = new ArrayList<>(environment.vhosts);
        this.provider = environment.provider;
        this.gatewayType = environment.gatewayType;
        this.additionalProperties = new HashMap<>(environment.additionalProperties);
        this.visibilityRoles = environment.visibilityRoles;
        this.visibility = environment.visibility;
        this.permissions = environment.permissions;
    }
    private GatewayVisibilityPermissionConfigurationDTO permissions = new GatewayVisibilityPermissionConfigurationDTO();

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public String getWebsocketGatewayEndpoint() {
        return websocketGatewayEndpoint;
    }

    public void setWebsocketGatewayEndpoint(String websocketGatewayEndpoint) {
        this.websocketGatewayEndpoint = websocketGatewayEndpoint;
    }

    public String getWebSubGatewayEndpoint() {
        return webSubGatewayEndpoint;
    }

    public void setWebSubGatewayEndpoint(String webSubGatewayEndpoint) {
        this.webSubGatewayEndpoint = webSubGatewayEndpoint;
    }

    public boolean isShowInConsole() {
        return showInConsole;
    }

    public void setShowInConsole(boolean showInConsole) {
        this.showInConsole = showInConsole;
    }

    private boolean showInConsole = true;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getServerURL() {
        return serverURL;
    }

    public void setServerURL(String serverURL) {
        this.serverURL = serverURL;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApiGatewayEndpoint() {
        return apiGatewayEndpoint;
    }

    public void setApiGatewayEndpoint(String apiGatewayEndpoint) {
        this.apiGatewayEndpoint = apiGatewayEndpoint;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
        if (StringUtils.isEmpty(this.uuid)) {
            this.uuid = name;
        }
        if (StringUtils.isEmpty(this.displayName)) {
            this.displayName = name;
        }
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public String[] getVisibilityRoles() {
        if (visibilityRoles != null) {
            return visibilityRoles;
        } else if (visibility != null) {
            return visibility.split(",");
        }
        return null;
    }

    public void setVisibility(String[] visibilityRoles) {
        if (visibilityRoles != null && !"".equals(visibilityRoles[0].trim())) {
            StringBuilder builder = new StringBuilder();
            for (String role : visibilityRoles) {
                builder.append(role).append(',');
            }
            builder.deleteCharAt(builder.length() - 1);
            this.visibility = builder.toString();
        } else {
            this.visibility = "PUBLIC";
            this.visibilityRoles[0] = "internal/everyone";
        }
        this.visibilityRoles = visibilityRoles;
    }

    public GatewayVisibilityPermissionConfigurationDTO getPermissions() {
        return permissions;
    }

    public void setPermissions(GatewayVisibilityPermissionConfigurationDTO permissions) {
        if (permissions == null) {
            permissions = new GatewayVisibilityPermissionConfigurationDTO();
        }
        this.permissions = permissions;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isReadOnly() {
        return isReadOnly;
    }

    public void setReadOnly(boolean readOnly) {
        isReadOnly = readOnly;
    }

    public List<VHost> getVhosts() {
        return vhosts;
    }

    public void setVhosts(List<VHost> vhosts) {
        this.vhosts = vhosts;
        // set gateway endpoint if it is empty
        if (StringUtils.isEmpty(apiGatewayEndpoint) && StringUtils.isEmpty(websocketGatewayEndpoint) && !vhosts.isEmpty()) {
            VHost vhost = vhosts.get(0);
            String endpointFormat = "%s%s:%s%s"; // {protocol}://{host}:{port}/{context}

            String httpContext = StringUtils.isEmpty(vhost.getHttpContext()) ? "" : "/" + vhost.getHttpContext();
            String gwHttpEndpoint = String.format(endpointFormat, APIConstants.HTTP_PROTOCOL_URL_PREFIX,
                    vhost.getHost(), vhost.getHttpPort(), httpContext);
            String gwHttpsEndpoint = String.format(endpointFormat, APIConstants.HTTPS_PROTOCOL_URL_PREFIX,
                    vhost.getHost(), vhost.getHttpsPort(), httpContext);
            apiGatewayEndpoint = gwHttpsEndpoint + "," + gwHttpEndpoint;

            String gwWsEndpoint = String.format(endpointFormat, APIConstants.WS_PROTOCOL_URL_PREFIX,
                    vhost.getHost(), vhost.getWsPort(), "");
            String gwWssEndpoint = String.format(endpointFormat, APIConstants.WSS_PROTOCOL_URL_PREFIX,
                    vhost.getHost(), vhost.getWssPort(), "");
            websocketGatewayEndpoint = gwWssEndpoint + "," + gwWsEndpoint;
        }
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getGatewayType() {
        return gatewayType;
    }

    public void setGatewayType(String gatewayType) {
        this.gatewayType = gatewayType;
    }

    public Map<String, String> getAdditionalProperties() {
        return additionalProperties;
    }

    public void setAdditionalProperties(Map<String, String> additionalProperties) {
        this.additionalProperties = additionalProperties;
    }

    public void setEndpointsAsVhost() throws APIManagementException {
        // Prefix websub endpoints with 'websub_', since API type will be identified with this URL.
        String modifiedWebSubGatewayEndpoint = webSubGatewayEndpoint.replaceAll("http://", "websub_http://")
                .replaceAll("https://", "websub_https://");
        String[] endpoints = (apiGatewayEndpoint + "," + websocketGatewayEndpoint + "," + modifiedWebSubGatewayEndpoint)
                .split(",", 6);
        getVhosts().add(VHost.fromEndpointUrls(endpoints));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Environment that = (Environment) o;

        if (!getName().equals(that.getName())) return false;
        if (!type.equals(that.getType())) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = type.hashCode();
        return  31 * result + getName().hashCode();
    }
}
