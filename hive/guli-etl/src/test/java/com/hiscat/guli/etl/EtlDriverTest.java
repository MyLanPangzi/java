package com.hiscat.guli.etl;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EtlDriverTest {

    @Test
    void testExtract() {
        String data = "LKh7zAJ4nwo\tTheReceptionist\t653\tEntertainment\t424\t13021\t4.34\t1305\t744\tDjdA-5oKYFQ\tNxTDlnOuybo\tc-8VuICzXtU\tDH56yrIO5nI\tW1Uo5DQTtzc\tE-3zXq_r4w0\t1TCeoRPg5dE\tyAr26YhuYNY\t2ZgXx72XmoE\t-7ClGo-YgZ0\tvmdPOOd6cxI\tKRHfMQqSHpk\tpIMpORZthYw\t1tUDzOp10pk\theqocRij5P0\t_XIuvoH6rUg\tLGVU5DsezE0\tuO2kj6_D8B4\txiDqywcDQRM\tuX81lMev6_o\n";
        assertNotNull(EtlDriver.extract("LKh7zAJ4nwo\tTheReceptionist\t653\tEntertainment\t424\t13021\t4.34\t1305\t744"));
        assertNull(EtlDriver.extract(""));
        assertEquals(EtlDriver.extract("LKh7zAJ4nwo\tTheReceptionist\t653\tEntertainment\t424\t13021\t4.34\t1305\t744"),"LKh7zAJ4nwo\tTheReceptionist\t653\tEntertainment\t424\t13021\t4.34\t1305\t744");
        assertEquals("LKh7zAJ4nwo\tTheReceptionist\t653\tEntertainment\t424\t13021\t4.34\t1305\t744\tDjdA-5oKYFQ&NxTDlnOuybo&c-8VuICzXtU",EtlDriver.extract("LKh7zAJ4nwo\tTheReceptionist\t653\tEntertainment\t424\t13021\t4.34\t1305\t744\tDjdA-5oKYFQ\tNxTDlnOuybo\tc-8VuICzXtU"));
    }
}