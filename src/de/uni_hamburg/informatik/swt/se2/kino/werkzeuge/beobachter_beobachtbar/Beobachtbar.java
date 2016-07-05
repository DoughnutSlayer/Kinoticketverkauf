package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.beobachter_beobachtbar;

import java.util.ArrayList;
import java.util.List;

/**
 * Beobachtbar vererbt die nötige Funktionalität für beobachtbare Klassen.
 * Dies beinhaltet das Registrieren und Benachrichtigen von Beobachtern.
 * 
 * @author TMNT
 * @version 08.06.2016
 */
public abstract class Beobachtbar
{
    protected List<Beobachter> _alleBeobachter; // Eine Liste die Referenzen auf sämtliche Beobachter des Objekts enthält.

    /**
     * Ein Konstruktor, welcher die Liste mit den Beobachtern initialisiert.
     */
    public Beobachtbar()
    {
        _alleBeobachter = new ArrayList<Beobachter>(1);
    }

    /**
     * @param beobachter Beobachter der über Änderungen am Objekt informiert werden soll.
     * 
     * @require beobachter != null
     */
    public void registriereBeobachter(Beobachter beobachter)
    {
        assert beobachter != null : "Vorbedingung verletzt: beobachter != null";
        _alleBeobachter.add(beobachter);
    }

    /**
     * Informiert sämtliche Beobachter über Aenderungen am Objekt.
     */
    protected void informiereUeberAenderung()
    {
        for (Beobachter beobachter : _alleBeobachter)
        {
            beobachter.reagiereAufAenderung(this);
        }
    }
}
