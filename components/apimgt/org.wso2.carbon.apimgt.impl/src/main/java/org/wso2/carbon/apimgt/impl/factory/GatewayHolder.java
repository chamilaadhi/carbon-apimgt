package org.wso2.carbon.apimgt.impl.factory;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.APIManagementException;
import org.wso2.carbon.apimgt.api.model.Environment;
import org.wso2.carbon.apimgt.api.model.GatewayAgentConfiguration;
import org.wso2.carbon.apimgt.api.model.GatewayConfiguration;
import org.wso2.carbon.apimgt.api.model.GatewayDeployer;
import org.wso2.carbon.apimgt.api.model.KeyManager;
import org.wso2.carbon.apimgt.impl.APIConstants;
import org.wso2.carbon.apimgt.impl.dto.GatewayDto;
import org.wso2.carbon.apimgt.impl.dto.KeyManagerDto;
import org.wso2.carbon.apimgt.impl.dto.OrganizationGatewayDto;
import org.wso2.carbon.apimgt.impl.dto.OrganizationKeyManagerDto;
import org.wso2.carbon.apimgt.impl.internal.ServiceReferenceHolder;
import org.wso2.carbon.apimgt.impl.loader.KeyManagerConfigurationDataRetriever;
import org.wso2.carbon.apimgt.impl.utils.APIUtil;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

public class GatewayHolder {
    private static Log log = LogFactory.getLog(GatewayHolder.class);
    private static final Map<String, OrganizationGatewayDto> organizationWiseMap = new HashMap<>();

    public static void addGatewayConfiguration(String organization, String name, String type,
                   GatewayConfiguration gatewayConfiguration) throws APIManagementException {

        OrganizationGatewayDto organizationGatewayDto = getTenantGatewayDtoFromMap(organization);
        if (organizationGatewayDto == null) {
            organizationGatewayDto = new OrganizationGatewayDto();
        }

        if (organizationGatewayDto.getGatewayByName(name) != null) {
            log.warn("Gateway " + name + " already initialized in tenant " + organization);
        }

        GatewayDeployer deployer = null;
        GatewayAgentConfiguration gatewayAgentConfiguration = ServiceReferenceHolder.getInstance().
                getExternalGatewayConnectorConfiguration(type);
        if (gatewayAgentConfiguration != null) {
            if (StringUtils.isNotEmpty(gatewayAgentConfiguration.getImplementation())) {
                try {
                    deployer = (GatewayDeployer) Class.forName(gatewayAgentConfiguration.getImplementation())
                            .getDeclaredConstructor().newInstance();
                    deployer.init(gatewayConfiguration);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException
                         | NoSuchMethodException | InvocationTargetException e) {
                    throw new APIManagementException("Error while loading gateway configuration", e);
                }
            }
        }

        GatewayDto gatewayDto = new GatewayDto();
        gatewayDto.setName(name);
        gatewayDto.setGatewayDeployer(deployer);
        organizationGatewayDto.putGatewayDto(gatewayDto);
        organizationWiseMap.put(organization, organizationGatewayDto);
    }

    public static void updateGatewayConfiguration(String organization, String name, String type,
                       GatewayConfiguration gatewayConfiguration) throws APIManagementException {

        removeGatewayConfiguration(organization, name);
        addGatewayConfiguration(organization, name, type, gatewayConfiguration);
    }

    public static void removeGatewayConfiguration(String tenantDomain, String name) {

        OrganizationGatewayDto organizationGatewayDto = getTenantGatewayDtoFromMap(tenantDomain);
        if (organizationGatewayDto != null) {
            organizationGatewayDto.removeGatewayDtoByName(name);
        }
    }

    public static GatewayDeployer getTenantGatewayInstance(String tenantDomain, String gatewayName) {

        OrganizationGatewayDto organizationGatewayDto = getTenantGatewayDto(tenantDomain);
        if (organizationGatewayDto != null) {
            GatewayDto gatewayDto = organizationGatewayDto.getGatewayByName(gatewayName);
            if (gatewayDto == null) {
                return null;
            }
            return gatewayDto.getGatewayDeployer();
        }
        return null;
    }

    private static OrganizationGatewayDto getTenantGatewayDto(String tenantDomain) {

        OrganizationGatewayDto organizationGatewayDto = getTenantGatewayDtoFromMap(tenantDomain);
        if (organizationGatewayDto == null) {
            try {
                Map<String, Environment> environmentMap = APIUtil.getEnvironments(tenantDomain);
                OrganizationGatewayDto newOrganizationGatewayDto = new OrganizationGatewayDto();
                for (Map.Entry<String, Environment> entry : environmentMap.entrySet()) {
                    Environment environment = entry.getValue();
                    if (environment.getProvider().equals(APIConstants.EXTERNAL_GATEWAY_VENDOR)) {
                        GatewayDto gatewayDto = new GatewayDto();
                        gatewayDto.setName(entry.getKey());
                        GatewayAgentConfiguration gatewayAgentConfiguration = ServiceReferenceHolder.getInstance().
                                getExternalGatewayConnectorConfiguration(entry.getValue().getGatewayType());
                        GatewayDeployer deployer = (GatewayDeployer) Class.forName(gatewayAgentConfiguration.getImplementation())
                                .getDeclaredConstructor().newInstance();
                        deployer.init(APIUtil.extractGatewayConfiguration(entry.getValue(), tenantDomain));
                        gatewayDto.setGatewayDeployer(deployer);
                        newOrganizationGatewayDto.putGatewayDto(gatewayDto);
                    }
                }
                organizationWiseMap.put(tenantDomain, newOrganizationGatewayDto);
                return newOrganizationGatewayDto;
            } catch (APIManagementException | ClassNotFoundException | IllegalAccessException | InstantiationException
                     | NoSuchMethodException | InvocationTargetException e) {
                log.error("Error while loading environments for tenant " + tenantDomain, e);
                return null;
            }
        }
        return organizationGatewayDto;
    }

    private static OrganizationGatewayDto getTenantGatewayDtoFromMap(String tenantDomain) {
        return organizationWiseMap.get(tenantDomain);
    }
}
