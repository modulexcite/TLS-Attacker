/**
 * TLS-Attacker - A Modular Penetration Testing Framework for TLS
 *
 * Copyright 2014-2017 Ruhr University Bochum / Hackmanit GmbH
 *
 * Licensed under Apache License 2.0
 * http://www.apache.org/licenses/LICENSE-2.0
 */
package de.rub.nds.tlsattacker.core.record;

import de.rub.nds.tlsattacker.core.config.Config;
import de.rub.nds.tlsattacker.core.constants.ChooserType;
import de.rub.nds.tlsattacker.core.constants.ProtocolMessageType;
import de.rub.nds.tlsattacker.core.constants.ProtocolVersion;
import de.rub.nds.tlsattacker.core.record.crypto.Encryptor;
import de.rub.nds.tlsattacker.core.record.parser.BlobRecordParser;
import de.rub.nds.tlsattacker.core.record.preparator.BlobRecordPreparator;
import de.rub.nds.tlsattacker.core.record.serializer.BlobRecordSerializer;
import de.rub.nds.tlsattacker.core.state.TlsContext;
import de.rub.nds.tlsattacker.core.workflow.chooser.Chooser;
import de.rub.nds.tlsattacker.core.workflow.chooser.ChooserFactory;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author Robert Merget <robert.merget@rub.de>
 */
public class BlobRecordTest {

    private BlobRecord record;
    private Chooser chooser;
    private Encryptor encryptor;

    @Before
    public void setUp() {
        record = new BlobRecord(Config.createConfig());
        chooser = ChooserFactory.getChooser(ChooserType.DEFAULT, new TlsContext());

    }

    /**
     * Test of getRecordPreparator method, of class BlobRecord.
     */
    @Test
    public void testGetRecordPreparator() {
        assertEquals(record.getRecordPreparator(chooser, encryptor, ProtocolMessageType.ALERT).getClass(),
                BlobRecordPreparator.class);
    }

    /**
     * Test of getRecordParser method, of class BlobRecord.
     */
    @Test
    public void testGetRecordParser() {
        assertEquals(record.getRecordParser(0, new byte[0], ProtocolVersion.TLS10).getClass(), BlobRecordParser.class);
        assertEquals(record.getRecordParser(0, new byte[0], ProtocolVersion.TLS11).getClass(), BlobRecordParser.class);
        assertEquals(record.getRecordParser(0, new byte[0], ProtocolVersion.TLS12).getClass(), BlobRecordParser.class);
        assertEquals(record.getRecordParser(0, new byte[0], ProtocolVersion.TLS13).getClass(), BlobRecordParser.class);
    }

    /**
     * Test of getRecordSerializer method, of class BlobRecord.
     */
    @Test
    public void testGetRecordSerializer() {
        assertEquals(record.getRecordSerializer().getClass(), BlobRecordSerializer.class);
    }

    /**
     * Test of adjustContext method, of class BlobRecord.
     */
    @Test
    public void testAdjustContext() {
    }

}