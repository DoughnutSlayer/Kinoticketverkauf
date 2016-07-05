package de.uni_hamburg.informatik.swt.se2.kino.fachwerte;

import org.junit.Test;

public class GeldbetragTest
{
    private Geldbetrag _dreißigCentInt = Geldbetrag.getGeldbetrag(30);
    private Geldbetrag _fünfEuroFünfundzwanzigString = Geldbetrag.getGeldbetrag("5,25");
    
    private Geldbetrag _zehnEuroInt = Geldbetrag.getGeldbetrag(1000);
    private Geldbetrag _zehnEuroString = Geldbetrag.getGeldbetrag("10,00");

    @Test
    public void gleichheitVonGeldbeträgenPrüfen()
    {
        Geldbetrag gb1 = Geldbetrag.getGeldbetrag(1000);
        Geldbetrag gb2 = Geldbetrag.getGeldbetrag("10,00");
        
        assertEquals(gb1, gb2);
    }
    
    @Test
    public void testeStringRepräsentation()
    {
        assertEquals("0,30", _dreißigCentInt.toString());
        assertEquals("5,25", _fünfEuroFünfundzwanzigString.toString());
    }
    
    @Test
    public void addieren()
    {
        Geldbetrag fünfEuroFünfundfünfzig = _fünfEuroFünfundzwanzigString.addiere(_dreißigCentInt);
        
        assertEquals(Geldbetrag.getGeldbetrag(555), fünfEuroFünfundfünfzig);
    }
    
    @Test
    public void subtrahieren()
    {
        Geldbetrag neunEuroSiebzig = _zehnEuroInt.subtrahiere(_dreißigCentInt);
        
        assertEquals(Geldbetrag.getGeldbetrag("9,70"), fünfEuroFünfundfünfzig);
    }
    
    @Test
    public void multiplizieren()
    {
        Geldbetrag einundzwanzigEuro = _fünfEuroFünfundzwanzigString.multipliziere(4);
        
        assertEquals(Geldbetrag.getGeldbetrag(2100), einundzwanzigEuro);
    }
}
