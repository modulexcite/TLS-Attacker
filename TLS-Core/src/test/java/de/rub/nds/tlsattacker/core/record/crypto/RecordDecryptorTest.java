/**
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2017 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.tlsattacker.core.record.crypto;

import de.rub.nds.modifiablevariable.util.ArrayConverter;
import de.rub.nds.tlsattacker.core.constants.CipherSuite;
import de.rub.nds.tlsattacker.core.constants.ProtocolMessageType;
import de.rub.nds.tlsattacker.core.constants.ProtocolVersion;
import de.rub.nds.tlsattacker.core.record.Record;
import de.rub.nds.tlsattacker.core.record.cipher.RecordAEADCipher;
import de.rub.nds.tlsattacker.core.record.cipher.RecordCipher;
import de.rub.nds.tlsattacker.core.socket.OutboundConnection;
import de.rub.nds.tlsattacker.core.state.TlsContext;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Nurullah Erinola <nurullah.erinola@rub.de>
 */
public class RecordDecryptorTest {

    private RecordCipher recordCipher;
    private TlsContext context;
    private Record record;
    public RecordDecryptor decryptor;

    public RecordDecryptorTest() {
    }

    @Before
    public void setUp() {
        context = new TlsContext();
        record = new Record();
    }

    /**
     * Test of the decrypt method for TLS 1.3, of class RecordDecryptor.
     */
    @Test
    public void testDecrypt() {
        context.setSelectedProtocolVersion(ProtocolVersion.TLS13);
        context.setSelectedCipherSuite(CipherSuite.TLS_AES_128_GCM_SHA256);
        context.setClientHandshakeTrafficSecret(ArrayConverter
                .hexStringToByteArray("4B63051EABCD514D7CB6D1899F472B9F56856B01BDBC5B733FBB47269E7EBDC2"));
        context.setServerHandshakeTrafficSecret(ArrayConverter
                .hexStringToByteArray("ACC9DB33EE0968FAE7E06DAA34D642B146092CE7F9C9CF47670C66A0A6CE1C8C"));
        context.setConnection(new OutboundConnection());
        record.setProtocolMessageBytes(ArrayConverter
                .hexStringToByteArray("1BB3293A919E0D66F145AE830488E8D89BE5EC16688229"));
        recordCipher = new RecordAEADCipher(context);
        decryptor = new RecordDecryptor(recordCipher, context);
        decryptor.decrypt(record);
        assertTrue(record.getContentMessageType() == ProtocolMessageType.HANDSHAKE);
        assertTrue(record.getCleanProtocolMessageBytes().getValue().length == 6);
        assertArrayEquals(record.getCleanProtocolMessageBytes().getValue(),
                ArrayConverter.hexStringToByteArray("080000020000"));
    }

}
