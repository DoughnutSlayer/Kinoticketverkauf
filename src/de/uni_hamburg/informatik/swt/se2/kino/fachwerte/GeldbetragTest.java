package de.uni_hamburg.informatik.swt.se2.kino.fachwerte;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Testklasse für Geldbetrag.
 * 
 * @author TMNT
 * @version 05.07.2015
 */
public class GeldbetragTest
{
    private Geldbetrag _dreißigCentInt = Geldbetrag.select(30); // Ein Geldbetrag vom Wert 0,30€, der mit einem int ausgewählt wurde.
    private Geldbetrag _fünfEuroFünfundzwanzigString = Geldbetrag
        .select("5,25"); // Ein Geldbetrag vom Wert 5,25€, der mit einem String ausgewählt wurde.

    private Geldbetrag _zehnEuroInt = Geldbetrag.select(1000); // Ein Geldbetrag vom Wert 10,00€, der mit einem int ausgewählt wurde.
    private Geldbetrag _zehnEuroString = Geldbetrag.select("10,00"); // Ein Geldbetrag vom Wert 10,00€, der mit einem String ausgewählt wurde.

    /**
     * Testet, dass zwei seperat erstellte Geldbeträge mit dem gleichen Wert als gleich angesehen werden.
     */
    @Test
    public void gleichheitVonGeldbeträgenPrüfen()
    {
        assertEquals(_zehnEuroInt, _zehnEuroString);
    }

    /**
     * Testet, ob die String repräsentationen der Geldbeträge die gewünschte Form haben.
     */
    @Test
    public void testeStringRepräsentation()
    {
        assertEquals("0,30", _dreißigCentInt.toString());
        assertEquals("5,25", _fünfEuroFünfundzwanzigString.toString());
        assertEquals("0,99", Geldbetrag.select(".99")
            .toString());
        assertEquals("10,00", Geldbetrag.select("10,")
            .toString());
        assertEquals("3,70", Geldbetrag.select("3.7")
            .toString());
        assertEquals("0,01", Geldbetrag.select("0,01")
            .toString());
        assertEquals("0,00", Geldbetrag.select(".")
            .toString());
        assertEquals("0,00", Geldbetrag.select(" ")
            .toString());
        assertEquals("0,00", Geldbetrag.select("")
            .toString());
    }

    /**
     * Testet ob bei der Addition von zwei Geldbeträgen das richtige Ergebnis herauskommt.
     * Testet zusätzlich, dass die Addition von Geldbeträgen fehlschlägt, wenn das Ergebnis negativ wäre.
     */
    @Test
    public void addieren()
    {
        Geldbetrag fünfEuroFünfundfünfzig = _fünfEuroFünfundzwanzigString
            .plus(_dreißigCentInt);
        boolean zuHoheAdditionUnmoeglich = false;

        assertEquals(Geldbetrag.select(555), fünfEuroFünfundfünfzig);

        try
        {
            _fünfEuroFünfundzwanzigString
                .plus(Geldbetrag.select(Integer.MAX_VALUE));
        }
        catch (AssertionError e)
        {
            System.out.println("Addition fehlgeschlagen!");
            zuHoheAdditionUnmoeglich = true;
        }

        assertTrue(zuHoheAdditionUnmoeglich);
    }

    /**
     * Testet ob bei Subtraktion zweier Geldbeträge das richtige Ergebnis herauskommt.
     * Testet zusätzlich, dass das Abziehen eines größeren Betrags von einem kleineren Betrag unmöglich ist.
     */
    @Test
    public void subtrahieren()
    {
        Geldbetrag neunEuroSiebzig = _zehnEuroInt.minus(_dreißigCentInt);
        boolean subtraktionEinesGroesserenBetragsUnmoeglich = false;

        assertEquals(Geldbetrag.select("9,70"), neunEuroSiebzig);

        try
        {
            _fünfEuroFünfundzwanzigString.minus(_zehnEuroInt);
        }
        catch (AssertionError e)
        {
            System.out.println("Subtraktion fehlgeschlagen!");
            subtraktionEinesGroesserenBetragsUnmoeglich = true;
        }

        assertTrue(subtraktionEinesGroesserenBetragsUnmoeglich);
    }

    /**
     * Testet ob bei der Multiplikation eines Geldbetrages mit einem int-Wert das richtige Ergebnis herauskommt.
     * Testet außerdem, dass der Faktor nicht so groß sein darf, dass das Ergebnis größer als Integer.MAX_VALUE wäre. 
     */
    @Test
    public void multiplizieren()
    {
        Geldbetrag einundzwanzigEuro = _fünfEuroFünfundzwanzigString.mal(4);
        boolean multiplikationMitZuGrossemFaktorUnmoeglich = false;

        assertEquals(Geldbetrag.select(2100), einundzwanzigEuro);

        try
        {
            _fünfEuroFünfundzwanzigString.mal(Integer.MAX_VALUE);
        }
        catch (AssertionError e)
        {
            System.out.println("Multiplikation fehlgeschlagen!");
            multiplikationMitZuGrossemFaktorUnmoeglich = true;
        }

        assertTrue(multiplikationMitZuGrossemFaktorUnmoeglich);
    }

    /**
     * Testet, dass die Methode "groesserAls" korrekt funktioniert.
     */
    @Test
    public void vergleichen()
    {
        assertTrue(_zehnEuroInt.groesserAls(_fünfEuroFünfundzwanzigString));
        assertTrue(_zehnEuroInt.groesserAls(525));

        assertFalse(_dreißigCentInt.groesserAls(_zehnEuroString));
        assertFalse(_dreißigCentInt.groesserAls(100));

        
        assertTrue(_zehnEuroInt.groesserGleich(_fünfEuroFünfundzwanzigString));
        assertTrue(_zehnEuroInt.groesserGleich(1000));

        assertFalse(_dreißigCentInt.groesserGleich(_zehnEuroString));
        assertFalse(_dreißigCentInt.groesserGleich(100));
    }

    /**
     * Testet, dass nur korrekte Strings beim auswählen eines Geldbetrags erlaubt werden.
     */
    @Test
    public void eingabeStringErlaubtheitTest()
    {
        String wert1 = "10";
        String wert2 = "5,90";
        String wert3 = "24.8";
        String wert4 = ",40";
        String wert5 = "133,";
        String wert6 = "12,50,10";

        assertTrue(Geldbetrag.istErlaubterString(wert1));
        assertTrue(Geldbetrag.istErlaubterString(wert2));
        assertTrue(Geldbetrag.istErlaubterString(wert3));
        assertTrue(Geldbetrag.istErlaubterString(wert4));
        assertTrue(Geldbetrag.istErlaubterString(wert5));
        assertFalse(Geldbetrag.istErlaubterString(wert6));
    }
}
