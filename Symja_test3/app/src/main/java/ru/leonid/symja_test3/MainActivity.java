package ru.leonid.symja_test3;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import static org.matheclipse.core.expression.F.*;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.math.MathException;

public class MainActivity extends AppCompatActivity {


	public void test() {
		try {
			// don't distinguish between lower- and uppercase identifiers
			Config.PARSER_USE_LOWERCASE_SYMBOLS = true;

			ExprEvaluator util = new ExprEvaluator(false, 100);

			// Show an expression in the Java form:
			// Note: single character identifiers are case sensistive
			// (the "D()" function input must be written as upper case character)
			String javaForm = util.toJavaForm("D(sin(x)*cos(x),x)");
			// prints: D(Times(Sin(x),Cos(x)),x)

			((TextView)findViewById(R.id.TextView)).setText(javaForm.toString());



		} catch (SyntaxError e) {
			// catch Symja parser errors here
			System.out.println(e.getMessage());
		} catch (MathException me) {
			// catch Symja math errors here
			System.out.println(me.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		test();
	}
}
