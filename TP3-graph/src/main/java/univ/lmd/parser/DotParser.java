package univ.lmd.parser;

import static org.petitparser.parser.primitive.CharacterParser.letter;
import static org.petitparser.parser.primitive.CharacterParser.of;

import java.util.ArrayList;

import org.petitparser.parser.Parser;
import org.petitparser.parser.primitive.CharacterParser;

public class DotParser {

        public static final Parser crochetOuvert = CharacterParser.of('[');
        public static final Parser crochetFerme = CharacterParser.of(']');
        public static final Parser parentheseBoucleeOuverte = CharacterParser.of('{');
        public static final Parser parenteseBoucleeFerme = CharacterParser.of('}');
        public static final Parser espace = of(' ').star();
        public static final Parser doubleQuote = CharacterParser.of('"');
        public static final Parser egale = CharacterParser.of('=');
        public static final Parser pointVirgule = CharacterParser.of(';');
        public static final Parser UneLettreMin = CharacterParser.letter().seq(letter().star());
        public static final Parser chaineCara = UneLettreMin.flatten().seq(espace).pick(0);
        public static final Parser fleche = CharacterParser.of('-').seq(CharacterParser.of('>'), espace).flatten();
        public static final Parser fleche2 = fleche.seq(chaineCara.seq(pointVirgule).pick(0)).pick(1);
        public static final Parser debutFichier = chaineCara.seq(chaineCara)
                        .seq(espace, parentheseBoucleeOuverte, espace, CharacterParser.of('\n'))
                        .pick(1);
        public static final Parser action = doubleQuote.seq(CharacterParser.noneOf("\""))
                        .seq(CharacterParser.noneOf("\"").star())
                        .seq(doubleQuote).flatten().seq(espace).pick(0);
        public static final Parser descriptionAction = crochetOuvert
                        .seq(espace, chaineCara, egale, espace, action, crochetFerme, espace)
                        .pick(5);
        public static final Parser finTransi = descriptionAction.seq(pointVirgule).pick(0);
        public static final Parser transition1 = chaineCara.seq(fleche)
                        .pick(0)
                        .seq(chaineCara, finTransi);
        public static final Parser transition2 = parentheseBoucleeOuverte
                        .seq(chaineCara, CharacterParser.of(','), espace, chaineCara, parenteseBoucleeFerme, espace)
                        .flatten()
                        .seq(fleche2);
        public static final Parser transitionParser = transition1.or(transition2).seq(CharacterParser.of('\n'))
                        .pick(0);
        public static final Parser fichier = debutFichier.seq(transitionParser.star()).seq();

        // fonction de parsing
        public ArrayList<Object> parse(String text) {
                return fichier.parse(text).get();
        }
}
