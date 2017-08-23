/**
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2017 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.tlsattacker.core.protocol.parser.extension;

import de.rub.nds.tlsattacker.core.constants.ExtensionByteLength;
import de.rub.nds.tlsattacker.core.protocol.message.extension.AlpnExtensionMessage;

/**
 *
 * @author Matthias Terlinde <matthias.terlinde@rub.de>
 */
public class AlpnExtensionParser extends ExtensionParser<AlpnExtensionMessage> {

    public AlpnExtensionParser(int startposition, byte[] array) {
        super(startposition, array);
    }

    @Override
    public void parseExtensionMessageContent(AlpnExtensionMessage msg) {
        msg.setAlpnExtensionLength(parseIntField(ExtensionByteLength.ALPN_EXTENSION_LENGTH));
        msg.setAlpnAnnouncedProtocols(parseByteArrayField(msg.getAlpnExtensionLength().getValue()));
    }

    @Override
    protected AlpnExtensionMessage createExtensionMessage() {
        return new AlpnExtensionMessage();
    }

}