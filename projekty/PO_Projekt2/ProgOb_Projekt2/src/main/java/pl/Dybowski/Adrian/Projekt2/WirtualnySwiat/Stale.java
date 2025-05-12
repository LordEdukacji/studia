package pl.Dybowski.Adrian.Projekt2.WirtualnySwiat;

import java.util.HashMap;
import java.util.Map;

import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.BarszczSosnowskiego;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.Guarana;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.Mlecz;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.Trawa;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Rosliny.WilczeJagody;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Antylopa;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Lis;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Owca;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Wilk;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Zolw;
import pl.Dybowski.Adrian.Projekt2.WirtualnySwiat.Zwierzeta.Czlowiek;

public class Stale {
    public static final Map<Class<?>, Integer> sila = new HashMap<Class<?>, Integer>() {{
        put(Wilk.class, 9);
        put(Owca.class, 4);
        put(Lis.class, 3);
        put(Zolw.class, 2);
        put(Antylopa.class, 4);
        
        put(Trawa.class, 0);
        put(Mlecz.class, 0);
        put(Guarana.class, 0);
        put(WilczeJagody.class, 99);
        put(BarszczSosnowskiego.class, 10);
        
        put(Czlowiek.class, 5);
    }};
    
    public static final Map<Class<?>, Integer> inicjatywa = new HashMap<Class<?>, Integer>() {{
        put(Wilk.class, 5);
        put(Owca.class, 4);
        put(Lis.class, 7);
        put(Zolw.class, 1);
        put(Antylopa.class, 4);
        
        put(Trawa.class, 0);
        put(Mlecz.class, 0);
        put(Guarana.class, 0);
        put(WilczeJagody.class, 0);
        put(BarszczSosnowskiego.class, 0);
        
        put(Czlowiek.class, 4);
    }};
    
    public static final Map<Class<?>, Double> szansaRuchu = new HashMap<Class<?>, Double>() {{
        put(Wilk.class, 1.00);
        put(Owca.class, 1.00);
        put(Lis.class, 1.00);
        put(Zolw.class, 0.25);
        put(Antylopa.class, 1.00);
        
        put(Czlowiek.class, 1.00);
    }};
    
    public static final Map<Class<?>, Integer> zasiegRuchu = new HashMap<Class<?>, Integer>() {{
        put(Wilk.class, 1);
        put(Owca.class, 1);
        put(Lis.class, 1);
        put(Zolw.class, 1);
        put(Antylopa.class, 2);
        
        put(Czlowiek.class, 1);
    }};
    
    public static final Map<Class<?>, Double> szansaUcieczki = new HashMap<Class<?>, Double>() {{
        put(Wilk.class, 0.00);
        put(Owca.class, 0.00);
        put(Lis.class, 0.00);
        put(Zolw.class, 0.00);
        put(Antylopa.class, 0.50);
        
        put(Czlowiek.class, 0.00);
    }};
    
    public static final Map<Class<?>, Double> szansaRozmnazania = new HashMap<Class<?>, Double>() {{
        put(Wilk.class, 1.00);
        put(Owca.class, 1.00);
        put(Lis.class, 1.00);
        put(Zolw.class, 1.00);
        put(Antylopa.class, 1.00);
        
        put(Czlowiek.class, 0.00);
    }};
    
    public static final Map<Class<?>, Integer> probyRozprzestrzeniania = new HashMap<Class<?>, Integer>() {{
    	put(Trawa.class, 1);
    	put(Mlecz.class, 3);
    	put(Guarana.class, 1);
    	put(WilczeJagody.class, 1);
    	put(BarszczSosnowskiego.class, 1);
    }};
    
    public static final Map<Class<?>, Double> szansaRozprzestrzeniania = new HashMap<Class<?>, Double>() {{
    	put(Trawa.class, 0.08);
    	put(Mlecz.class, 0.08);
    	put(Guarana.class, 0.08);
    	put(WilczeJagody.class, 0.08);
    	put(BarszczSosnowskiego.class, 0.08);
    }};
    
    public static final int maxTrwanieZdolnosci = 5;
    public static final int maxOdnowienieZdolnosci = 5;
}
