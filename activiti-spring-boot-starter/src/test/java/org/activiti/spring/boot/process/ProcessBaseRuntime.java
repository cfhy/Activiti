/*
 * Copyright 2018 Alfresco, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.activiti.spring.boot.process;

import org.springframework.beans.factory.annotation.Autowired;
import org.activiti.spring.boot.security.util.SecurityUtil;
import org.activiti.api.process.model.ProcessInstance;
import org.activiti.api.process.runtime.ProcessRuntime;
import org.activiti.api.runtime.shared.query.Pageable;
import org.activiti.api.runtime.shared.query.Page;
import org.activiti.api.process.model.builders.ProcessPayloadBuilder;
import org.activiti.api.process.model.payloads.DeleteProcessPayload;
import org.activiti.api.process.model.payloads.GetProcessInstancesPayload;
import org.springframework.boot.test.context.TestComponent;
import org.activiti.api.model.shared.model.VariableInstance;
import java.util.List;

@TestComponent
public class ProcessBaseRuntime {

    @Autowired
    private ProcessRuntime processRuntime;

    @Autowired
    private SecurityUtil securityUtil;

    public ProcessInstance startProcessWithProcessDefinitionKey(String processDefinitionKey) {
        securityUtil.logInAs("user");
        return processRuntime.start(ProcessPayloadBuilder.start()
        .withProcessDefinitionKey(processDefinitionKey)
        .build());
    }

    public List<ProcessInstance> getProcessInstances() {
        List<ProcessInstance> processList = processRuntime
                .processInstances(Pageable.of(0, 50), ProcessPayloadBuilder.processInstances().build()).getContent();
        return processList;
    }
    public Page<ProcessInstance> getProcessInstancesPage() {
        return processRuntime.processInstances(Pageable.of(0, 50));
    }

    public List<VariableInstance> getProcessVariablesByProcessId(String processId) {
        return processRuntime.variables(ProcessPayloadBuilder.variables().withProcessInstanceId(processId).build());
    }

    public ProcessInstance delete(String processInstanceId) {
        return this.delete(processInstanceId, "");
    }

    public ProcessInstance delete(String processInstanceId, String reason) {
        return processRuntime.delete(new DeleteProcessPayload(processInstanceId, reason));
    }

}
