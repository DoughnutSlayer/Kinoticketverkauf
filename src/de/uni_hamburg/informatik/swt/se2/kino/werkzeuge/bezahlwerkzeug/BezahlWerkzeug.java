package de.uni_hamburg.informatik.swt.se2.kino.werkzeuge.bezahlwerkzeug;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;

import de.uni_hamburg.informatik.swt.se2.kino.fachwerte.Geldbetrag;

/**
 * Eine Klasse die den Verkauf von Kinotickets abwickelt.
 * 
 * @author TMNT
 * @version 05.07.2016
 */
public class BezahlWerkzeug
{
    private Geldbetrag _preis; // Der Gesamtpreis der zu bezahlenden Kinotickets.

    private boolean _bezahlt; // Ob die Kinotickets bezahlt wurden.

    private BezahlWerkzeugUI _ui; // Das UI dieses Werkzeugs.

    /**
     * Eröffnet den Bezahldialog mit dem User.
     * @param preis Der Gesamtpreis der zu verkaufenden Kinotickets.
     * @return Ob die Kinotickets bezahlt wurden.
     * 
     * @require Geldbetrag != null
     */
    public boolean rufeBezahlDialogAuf(Geldbetrag preis)
    {
        assert preis != null : "Vorbedingung verletzt: Geldbetrag != null";

        _preis = preis;

        _ui = new BezahlWerkzeugUI();
        _ui.getPreisFeld()
            .setText(erstelleBetragString(_preis));
        aktualisiereUI(Geldbetrag.select(0));
        registriereUIAktionen();

        _ui.oeffneDialog();

        return _bezahlt;
    }

    /**
     * @param betrag Ein Geldbetrag der zu einem String umgewandelt werden soll.
     * @return Ein String der einen EuroCent Betrag repräsentiert in der Form "10,25€".
     * 
     * @require betrag != null
     */
    private String erstelleBetragString(Geldbetrag betrag)
    {
        assert betrag != null : "Vorbedingung verletzt: betrag != null";
        return betrag.toString() + "€";
    }

    /**
     * Fügt die Funktionalität zu den Buttons und dem Eingabefeld des UIs hinzu.
     */
    private void registriereUIAktionen()
    {
        _ui.getOkButton()
            .addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    _bezahlt = true;
                    _ui.schliesseDialog();
                }
            });

        _ui.getAbbrechenButton()
            .addActionListener(new ActionListener()
            {
                @Override
                public void actionPerformed(ActionEvent e)
                {
                    _bezahlt = false;
                    _ui.schliesseDialog();
                }
            });

        _ui.getEingabefeld()
            .getDocument()
            .addDocumentListener(new DocumentListener()
            {
                @Override
                public void changedUpdate(DocumentEvent e)
                {
                }

                @Override
                public void removeUpdate(DocumentEvent e)
                {
                    reagiereAufEingabe(e);
                }

                @Override
                public void insertUpdate(DocumentEvent e)
                {
                    reagiereAufEingabe(e);
                }
            });
    }

    /**
     * Wenn der User etwas eingegeben hat, aktualisiert diese Methode das UI entsprechend.
     * @param e Das DocumentEvent das von dem Eingabefeld gesendet wurde.
     */
    private void reagiereAufEingabe(DocumentEvent e)
    {
        String eingabe = "";
        try
        {
            eingabe = e.getDocument()
                .getText(0, e.getDocument()
                    .getLength());
        }
        catch (BadLocationException excptn)
        {
        }

        if (!Geldbetrag.istErlaubterString(eingabe))
        {
            ungültigeEingabe();
        }
        else
        {
            aktualisiereUI(Geldbetrag.select(eingabe));
        }
    }

    /**
     * Aktualisiert das UI entsprechend dem eingegebenen Betrag.
     * @param gegeben Der vom Kunden gegebene Centbetrag.
     * 
     * @require gegeben != null
     */
    private void aktualisiereUI(Geldbetrag gegeben)
    {
        assert gegeben != null : "Vorbedingung verletzt: gegeben != null";

        boolean genugGegeben = gegeben.groesserGleich(_preis);
        Label rueckgeldLabel = _ui.getRueckgeldLabel();
        Label rueckgeldFeld = _ui.getRueckgeldFeld();

        if (genugGegeben)
        {
            rueckgeldLabel.setText("Rückgeld:   ");
            rueckgeldFeld.setText(erstelleBetragString(gegeben.minus(_preis)));
        }
        else
        {
            rueckgeldLabel.setText("Noch zu zahlen:   ");
            rueckgeldFeld.setText(erstelleBetragString(_preis.minus(gegeben)));
        }

        _ui.getOkButton()
            .setEnabled(genugGegeben);
    }

    private void ungültigeEingabe()
    {
        _ui.getRueckgeldLabel()
            .setText("");
        _ui.getRueckgeldFeld()
            .setText("Ungültige Eingabe");
    }
}
