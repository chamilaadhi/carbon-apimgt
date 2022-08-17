package org.wso2.carbon.apimgt.impl.workflow;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.wso2.carbon.apimgt.api.WorkflowResponse;
import org.wso2.carbon.apimgt.impl.dto.WorkflowDTO;

public class ApplicationCreationWebHookWorkflowExecutor extends WorkflowExecutor{

    private static final Log log = LogFactory.getLog(ApplicationCreationWebHookWorkflowExecutor.class);

    @Override
    public String getWorkflowType() {
        return WorkflowConstants.WF_TYPE_AM_APPLICATION_CREATION;
    }


	@Override
	public List<WorkflowDTO> getWorkflowDetails(String workflowStatus) throws WorkflowException {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public WorkflowResponse execute(WorkflowDTO workflowDTO) throws WorkflowException {
		log.info("++++++++++++++ " + workflowDTO.getExternalWorkflowReference());
        super.execute(workflowDTO);

        return new GeneralWorkflowResponse();
	}
	
	@Override
	public WorkflowResponse complete(WorkflowDTO workflowDTO) throws WorkflowException {
		log.info("++++++++++++++ complete ++++++++++++++++++");
		return super.complete(workflowDTO);
	}

}
