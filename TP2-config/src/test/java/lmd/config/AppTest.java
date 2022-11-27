package lmd.config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import lmd.config.exception.NonFieldException;
import lmd.config.exception.NonObjectException;
import lmd.config.model.Non;
import lmd.config.model.NonDefs;

/**
 * Unit test for simple App.
 */
public class AppTest 
{
    @Test
    public void checkValidCase() throws NonFieldException, NonObjectException
    {
        NonDefs nonDefs = Non.fromString("univ:\n.name 'Universite Exemple'\n.domain 'exemple.tld'");
        assertTrue( nonDefs.getNonObjects().size() == 1 );
        assertEquals("exemple.tld", nonDefs.at("univ").get("domain"));
    }

    @Test(expected = NonObjectException.class)
    public void shouldThrowNonObjectException() throws NonObjectException
    {
        NonDefs nonDefs = Non.fromString("univ:\n.name 'Universite Exemple'\n.domain 'exemple.tld'");
        nonDefs.at("test3");
    }

    @Test(expected = NonFieldException.class)
    public void shouldThrowNonFieldException() throws NonFieldException, NonObjectException
    {
        NonDefs nonDefs = Non.fromString("univ:\n.name 'Universite Exemple'\n.domain 'exemple.tld'");
        nonDefs.at("univ").get("test3");
    }

    @Test
    public void shouldWorkCorrectly() throws NonFieldException, NonObjectException
    {
        NonDefs nonDefs = Non.fromString("univ:\n.name 'Universite Exemple'\n.domain 'exemple.tld'\n"+
                                        "student:\n.mail .login '.etu@' univ.domain\n.name .login\n.login @\n"+
                                        "alice: student\n"+ "bob: student\n.name 'robert'");
        assertTrue( nonDefs.getNonObjects().size() == 4 );
        assertEquals("alice", nonDefs.at("alice").get("login"));
        assertEquals("alice", nonDefs.at("alice").get("name"));
        assertEquals("alice.etu@exemple.tld", nonDefs.at("alice").get("mail"));
        assertEquals("robert", nonDefs.at("bob").get("name"));
    }

}
