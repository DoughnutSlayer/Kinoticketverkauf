package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.beobachter_beobachtbar;

/**
 * Ein Interface für Objekte, die andere Objekte beobachten sollen.
 * 
 * @author TMNT
 * @version 08.06.2016
 */
public interface Beobachter
{
    /**
     * Reagiert auf Änderungen eines bestimmten, beobachteten Objekts.
     * 
     * @param beobachtbar Das beobachtete Objekt an dem eine Änderung stattgefunden hat.
     * 
     * @require beobachteter != null
     */
    public void reagiereAufAenderung(Beobachtbar beobachtbar);
}
