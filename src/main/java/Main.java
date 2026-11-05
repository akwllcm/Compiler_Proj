import framework.project4.ErrorReporter;
import framework.project4.Grader;
import generated.SplLexer;
import generated.Splc;
import impl.SemanticAnalyzer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

public final class Main {
    public static void main(String[] args) throws Exception {
        CharStream input = CharStreams.fromStream(System.in);
        SplLexer lexer = new SplLexer(input);
        lexer.removeErrorListeners();
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        Splc parser = new Splc(tokens);
        parser.removeErrorListeners();
        Splc.ProgramContext program = parser.program();

        ErrorReporter reporter = new ErrorReporter();
        SemanticAnalyzer analyzer = new SemanticAnalyzer(reporter);
        analyzer.analyze(program);
        Grader.print(reporter, analyzer.getGlobalScope());
    }
}
