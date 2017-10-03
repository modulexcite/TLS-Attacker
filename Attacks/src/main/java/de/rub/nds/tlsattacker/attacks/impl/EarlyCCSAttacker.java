/**
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2017 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.tlsattacker.attacks.impl;

import de.rub.nds.tlsattacker.attacks.config.EarlyCCSCommandConfig;
import de.rub.nds.tlsattacker.core.config.Config;
import de.rub.nds.tlsattacker.core.protocol.message.CertificateMessage;
import de.rub.nds.tlsattacker.core.protocol.message.ChangeCipherSpecMessage;
import de.rub.nds.tlsattacker.core.protocol.message.ClientHelloMessage;
import de.rub.nds.tlsattacker.core.protocol.message.ProtocolMessage;
import de.rub.nds.tlsattacker.core.protocol.message.ServerHelloDoneMessage;
import de.rub.nds.tlsattacker.core.protocol.message.ServerHelloMessage;
import de.rub.nds.tlsattacker.core.state.State;
import de.rub.nds.tlsattacker.core.state.TlsContext;
import de.rub.nds.tlsattacker.core.util.LogLevel;
import de.rub.nds.tlsattacker.core.workflow.WorkflowExecutor;
import de.rub.nds.tlsattacker.core.workflow.WorkflowExecutorFactory;
import de.rub.nds.tlsattacker.core.workflow.WorkflowTrace;
import de.rub.nds.tlsattacker.core.workflow.action.ReceiveAction;
import de.rub.nds.tlsattacker.core.workflow.action.SendAction;
import java.util.LinkedList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * TODO: currently does not work correctly, will be fixed after some
 * refactorings.
 *
 * @author Juraj Somorovsky (juraj.somorovsky@rub.de)
 */
public class EarlyCCSAttacker extends Attacker<EarlyCCSCommandConfig> {

    public static Logger LOGGER = LogManager.getLogger(EarlyCCSAttacker.class);

    public EarlyCCSAttacker(EarlyCCSCommandConfig config) {
        super(config, false);
    }

    @Override
    public void executeAttack() {
        // byte[] ms = new byte[48];
        // byte[] pms = new byte[48];
        // pms[0] = 3;
        // pms[1] = 3;
        // workflowTrace.addTlsAction(new ChangePreMasterSecretAction(pms));
        // workflowTrace.addTlsAction(new ChangeMasterSecretAction(ms));
        throw new UnsupportedOperationException("Not implemented yet");
    }

    @Override
    public Boolean isVulnerable() {
        Config tlsConfig = config.createConfig();
        tlsConfig.getDefaultClientConnection().setTimeout(1000);

        WorkflowTrace workflowTrace = new WorkflowTrace();
        workflowTrace.addTlsAction(new SendAction(new ClientHelloMessage(tlsConfig)));
        List<ProtocolMessage> messageList = new LinkedList<>();
        messageList.add(new ServerHelloMessage(tlsConfig));
        messageList.add(new CertificateMessage(tlsConfig));
        messageList.add(new ServerHelloDoneMessage(tlsConfig));
        workflowTrace.addTlsAction(new ReceiveAction(messageList));
        messageList = new LinkedList<>();
        messageList.add(new ChangeCipherSpecMessage());
        workflowTrace.addTlsAction(new SendAction(messageList));
        messageList = new LinkedList<>();
        workflowTrace.addTlsAction(new ReceiveAction(messageList));

        State state = new State(tlsConfig);
        state.setWorkflowTrace(workflowTrace);
        TlsContext tlsContext = state.getTlsContext();
        WorkflowExecutor workflowExecutor = WorkflowExecutorFactory.createWorkflowExecutor(
                tlsConfig.getWorkflowExecutorType(), state);

        workflowExecutor.executeWorkflow();
        if (tlsContext.isReceivedFatalAlert()) {
            LOGGER.log(LogLevel.CONSOLE_OUTPUT, "Not vulnerable (probably), no Alert message found");
            return false;
        } else {
            LOGGER.log(LogLevel.CONSOLE_OUTPUT, "Vulnerable (probably), Alert message found");
            return true;
        }
    }
}
