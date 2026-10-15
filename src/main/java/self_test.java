import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Interval;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import generated.SplLexer.SplLexer;

public class self_test {

    public static void main(String[] args) throws Exception {

        // 从文件读字符流（UTF-8）
        CharStream input = CharStreams.fromPath(Paths.get("in.txt"), StandardCharsets.UTF_8);

        // 构造 Lexer
        SplLexer lexer = new SplLexer(input);

        // 可选：挂一个错误监听，把词法错误打印到 stderr
        lexer.removeErrorListeners();
        lexer.addErrorListener(new BaseErrorListener() {
            @Override
            public void syntaxError(Recognizer<?, ?> recognizer,
                                    Object offendingSymbol,
                                    int line, int charPositionInLine,
                                    String msg, RecognitionException e) {
                System.err.printf("[LEXER ERROR] line %d:%d %s%n",
                        line, charPositionInLine, msg);
            }
        });

        // 填满 Token 流
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        tokens.fill();

        Vocabulary vocab = lexer.getVocabulary();

        // 逐个打印
        for (Token token : tokens.getTokens()) {
            String typeName = vocab.getSymbolicName(token.getType());
            if (typeName == null) {
                // 某些 Token 类型没有符号名，用类型号兜底
                typeName = String.valueOf(token.getType());
            }
            String raw = token.getText();
            // EOF 的文本通常是 "<EOF>"，也可自己替换
            System.out.printf("Token: %s,  Raw: %s%n", typeName, raw);
        }
    }
}