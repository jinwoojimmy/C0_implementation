import java.util.*;
import java.util.stream.Collectors;

/* CZeroLogicModule - main logic for the project  */
public class CZeroLogicModule {

    private static final String DOT_SYMBOL = ".";
    private static final String DOT_SYMBOL_SPLIT = "\\.";
    private static final String EPSILON = "";
    private ArrayList<ProductionRule> productionRules;
    private ArrayList<State> states;
    private ArrayList<LRZeroItem> closureItems;  // except for augmented rule's closure item
    private Set<String> nonTerminals;

    // singleton design pattern
    private static CZeroLogicModule instance;

    public static CZeroLogicModule getInstance() {
        if (instance == null)
            instance = new CZeroLogicModule();
        return instance;
    }
    // constructor
    public CZeroLogicModule() {
        // initialize array lists
        states = new ArrayList<State>();
        productionRules = new ArrayList<ProductionRule>();
        closureItems = new ArrayList<LRZeroItem>();
        nonTerminals = new HashSet<String>();
    }

    /* base rule for convenience */
    abstract class BaseRule {
        private String lhs;
        private String content;
        public String getLhs() {
            return this.lhs;
        }
        public String getContent() {
            return this.content;
        }
        public void setLhs(String lhs) {
            this.lhs = lhs;
        }
        public void setContent(String content) {
            this.content = content;
        }
    }


    /* production rule */
    class ProductionRule extends BaseRule {
        // lhs : derivated nonTerminal ex> E
        // content: full rule ex> E>E+T

        public ProductionRule(String rule) {
            setContent(rule);
            setLhs(String.valueOf(rule.charAt(0)));
        }
    }

    /* LR(0)  */
    class LRZeroItem extends BaseRule {
        // lhs
        // content : the production rule which has dot symbol in the right hand

        // create LR(0) from production rule
        public LRZeroItem(ProductionRule pr) {
            // reformat rule style  ; ex) S>E to S->.E
            String productionRule = pr.getContent();
            String[] temp = productionRule.split(">");

            setLhs(pr.getLhs());
            setContent(pr.getLhs() + "->." + temp[1]);
        }

        // copy constructor
        public LRZeroItem(LRZeroItem that) {
            setLhs(that.getLhs());
            setContent(that.getContent());
        }

        // shift by moving dot symbol
        public void shift() {

            String s = this.getContent();
            String[] temp = s.split(DOT_SYMBOL_SPLIT);
            String newContent = temp[0] + String.valueOf(temp[1].charAt(0)) +
                    DOT_SYMBOL + temp[1].substring(1);
            this.setContent(newContent);
        }

        // returns mark symbol of LR0
        public String getMarkSymbol() {
            String[] temp = this.getContent().split(DOT_SYMBOL_SPLIT);
            if (temp.length == 2)
                return String.valueOf(temp[1].charAt(0));
            return EPSILON; // when there is no mark symbol
        }

    }

    /* state  */
    private class State {
        private ArrayList<LRZeroItem> LRZeroItems;

        public State(ArrayList<LRZeroItem> LRZeroItems) {
            this.LRZeroItems = LRZeroItems;
        }

        public ArrayList<LRZeroItem> getLRZeroItems() {
            return this.LRZeroItems;
        }

        public ArrayList<String> getMarkSymbols() {

            ArrayList<String> markSymbolList = new ArrayList<String>();
            for (LRZeroItem lr: LRZeroItems) {
                String ms = lr.getMarkSymbol();
                if (!ms.equals(EPSILON) && !markSymbolList.contains(ms))
                    markSymbolList.add(ms);
            }

            return markSymbolList;
        }

        public ArrayList<LRZeroItem> getCorrespondingLRZeroItemsByMarkSymbol(String markSymbol) {
            ArrayList<LRZeroItem> list = new ArrayList<>();

            for (LRZeroItem lrzi: this.LRZeroItems) {
                if (lrzi.getMarkSymbol().equals(markSymbol))
                    list.add(lrzi);
            }
            return list;
        }


        // check if two states are eqaul
        public boolean equals(State that) {
            for(LRZeroItem a: this.getLRZeroItems()) {
                // if no equal content, then return false
                if (that.getLRZeroItems().stream().filter(lrzi -> lrzi.getContent().
                        equals(a.getContent())).collect(Collectors.toList()).size() == 0)
                    return false;
            }

            return true;
        }

    }


    // ex> GOTO(S_1, "+")
    public void GOTO(State state, String mark) {

        // get LR(0) items from state whose mark symbol is same as given symbol
        ArrayList<LRZeroItem> list = state.getCorrespondingLRZeroItemsByMarkSymbol(mark);

        // shift the DOT_SYMBOL
        for (int i = 0; i < list.size(); i++ ) {
            LRZeroItem lrzi = list.get(i);
            LRZeroItem newLrzi = new LRZeroItem(lrzi);
            newLrzi.shift();
            list.set(i, newLrzi);
        }

        CLOSURE(list);

    }


    public void CLOSURE(ArrayList<LRZeroItem> list) {

        int idx = 0;

        // with inserted LR(0), add possible Closure Item
        while(idx < list.size()) {

            LRZeroItem lrzi = list.get(idx);

            String markSymbol = lrzi.getMarkSymbol();

            // if mark symbol is non-terminal ; can be expanded more
            if (nonTerminals.contains(markSymbol)) {
                List<LRZeroItem> matchingClosureItems = closureItems.stream().filter(ci -> ci.getLhs().
                        equals(markSymbol)).collect(Collectors.toList());

                for (LRZeroItem closureItem: matchingClosureItems) {
                    // check if closure item is already included in the list
                    if (!listContainsLRZeroItem(list, closureItem))
                        list.add(closureItem);
                }
            }
            idx++;
        }
        // add state
        addNewState(new State(list));

    }


    // helper method
    public boolean listContainsLRZeroItem(ArrayList<LRZeroItem> list, LRZeroItem lrzi) {
        for(LRZeroItem t: list) {
            if (t.getContent().equals(lrzi.getContent())) {
                return true;
            }
        }
        return false;
    }

    // helper method to add new state
    public void addNewState(State tempState) {
        // if tempState in states, do not add!
        for (int i = 0; i < states.size() ; i++) {
            State state = states.get(i);
            if (tempState.equals(state))
                return;
        }
        states.add(tempState);

    }

    // when user try another rules, this method should be called
    public void reset() {
        states.clear();
        productionRules.clear();
        nonTerminals.clear();
        closureItems.clear();
    }

    public String constructOutput() {
        StringBuilder contentBuilder = new StringBuilder();
        int stateNum = 0;
        for (State state: states) {
            StringBuilder stringBuilder = new StringBuilder();

            contentBuilder.append("I"+String.valueOf(stateNum)+"\n");

            for (LRZeroItem lrzi: state.getLRZeroItems()) {
                stringBuilder.append("[");
                stringBuilder.append(lrzi.getContent());
                stringBuilder.append("] ");
            }
            stringBuilder.append("\n");

            contentBuilder.append(stringBuilder.toString());
            stateNum++;
        }

        return contentBuilder.toString();
    }

    // main
    public String logic(String content) {

        // parse rule from input content, and then create production rule instances
        String[] inputs = content.split("\n");
        for (int i = 1; i < inputs.length; i+=2) {
            // create production rule instance with string of no empty space
            ProductionRule productionRule = new ProductionRule(inputs[i].replaceAll("\\s+", ""));
            productionRules.add(productionRule);
            closureItems.add(new LRZeroItem(productionRule));
        }

        // add augmented rule R0
        ProductionRule R0 = new ProductionRule("S>" + productionRules.get(0).getLhs());
        productionRules.add(0, R0);

        // collect existing non-terminals
        for (ProductionRule pr: productionRules) {
            nonTerminals.add(pr.getLhs());
        }

        // create state I0
        ArrayList<LRZeroItem> list = new ArrayList<>();
        list.add(new LRZeroItem(R0));
        CLOSURE(list);  // addNewState(State tempState) is called inside the method

        // GOTO from I0
        int idx = 0;
        while ( idx < states.size() ) {
            State state = states.get(idx);
            ArrayList<String> markSymbols = state.getMarkSymbols();
            for (String mark : markSymbols) {
                GOTO(state, mark);
            }
            idx++;
        }

        // build string for the output
        String output = constructOutput();
        return output;

    }
}
