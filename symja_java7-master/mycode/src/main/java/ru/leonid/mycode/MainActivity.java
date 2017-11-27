package ru.leonid.mycode;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import static org.matheclipse.core.expression.F.*;

import org.matheclipse.core.basic.Config;
import org.matheclipse.core.convert.VariablesSet;
import org.matheclipse.core.eval.ExprEvaluator;
import org.matheclipse.core.interfaces.IAST;
import org.matheclipse.core.interfaces.IExpr;
import org.matheclipse.core.interfaces.INum;
import org.matheclipse.parser.client.SyntaxError;
import org.matheclipse.parser.client.eval.DoubleEvaluator;
import org.matheclipse.parser.client.math.MathException;

import java.util.Random;


public class MainActivity extends AppCompatActivity {
	EditText editText;
	static DoubleEvaluator evaluator;
	static ExprEvaluator util;
	static Random r;

	static double eval(IExpr expr)
	{
		return evaluator.evaluate(util.eval(expr).toString());
	}

	IAST findRoots(IAST function) throws MathException
	{
		IAST vars = VariablesSet.getVariables(function);
		IAST grad = (IAST) util.eval(D(function,List(vars)));

		INum LR = num(0.01);// Diverging rate
		double max_error = 0.001;

		// trying not to hit local minima "tries" times
		final int tries = 100;
		for(int j = 0; j < tries; j++)
		{
			boolean minima = false, infinity = false;
			double err = 1, prev_err = 0;

			int size = (int)eval(Length(vars));
			for(int k = 1; k <= size; k++)
				util.eval(Set(Slot(k), num(r.nextFloat()*10-5)));

			// Gradient descent
			for (int i = 0; Math.abs(err) > max_error;i++)
			{
				util.eval(Set(vars, Plus(vars, Times( err > 0 ? Negate(grad) : grad, LR))));
				if(err == prev_err)
				{
					minima = true;
					break;
				}
				if(i > 10000)
				{
					infinity = true;
					break;
				}
				prev_err = err;
				err = eval(function);
			}

			if(minima && j == tries-1)
				throw new MathException("No roots!");
			if(infinity && j == tries-1)
				throw new MathException("Bad function!");
			if(!minima && !infinity)
				break;
		}

		/*System.out.print("Iters: ");
		System.out.print(i);
		System.out.print("  Roots: ");
		System.out.println(util.eval(vars).toString());
		System.out.print("Error: ");
		System.out.println(err);
		System.out.println("");
		 */

		return vars;
	}

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
			editText.append("   ");
			editText.append(javaForm.toString());

			// Use the Java form to create an expression with F.* static methods:
			IAST function = D(Times(Sin(x), Cos(x)), x);
			IExpr result = util.evaluate(function);
			// print: Cos(x)^2-Sin(x)^2
			editText.append("   ");
			editText.append(result.toString());

			// evaluate the string directly
			// Note "diff" is an alias for the "D" function
			result = util.evaluate("diff(sin(x)*cos(x),x)");
			// print: Cos(x)^2-Sin(x)^2
			editText.append("   ");
			editText.append(result.toString());

			// evaluate the last result ($ans contains "last answer")
			result = util.evaluate("$ans+cos(x)^2");
			// print: 2*Cos(x)^2-Sin(x)^2
			editText.append("   ");
			editText.append(result.toString());

			// evaluate an Integrate[] expression
			result = util.evaluate("integrate(sin(x)^5,x)");
			// print: 2/3*Cos(x)^3-1/5*Cos(x)^5-Cos(x)
			editText.append("   ");
			editText.append(result.toString());

			// set the value of a variable "a" to 10
			// Note: in server mode the variable name must have a preceding '$' character
			result = util.evaluate("a=10");
			// print: 10
			editText.append("   ");
			editText.append(result.toString());

			// do a calculation with variable "a"
			result = util.evaluate("a*3+b");
			// print: 30+b
			editText.append("   ");
			editText.append(result.toString());

			// Do a calculation in "numeric mode" with the N() function
			// Note: single character identifiers are case sensistive
			// (the "N()" function input must be written as upper case character)
			result = util.evaluate("N(sinh(5))");
			// print: 74.20321057778875
			editText.append("   ");
			editText.append(result.toString());

			// define a function with a recursive factorial function definition.
			// Note: fac(0) is the stop condition which must be defined first.
			result = util.evaluate("fac(0)=1;fac(x_IntegerQ):=x*fac(x-1)");
			// now calculate factorial of 10:
			result = util.evaluate("fac(10)");
			// print: 3628800
			editText.append("   ");
			editText.append(result.toString());

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

	void println(String str)
	{
		str = str.replace("#1", "x");
		str = str.replace("#2", "y");
		editText.setText(editText.getText() + "\n" + str);
	}
	void println(IExpr expr)
	{
		println(expr.toString());
	}
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		editText = (EditText)findViewById(R.id.editText);

		r = new Random(System.currentTimeMillis());
		// don't distinguish between lower- and uppercase identifiers
		Config.PARSER_USE_LOWERCASE_SYMBOLS = true;

		util = new ExprEvaluator(false, 100);
		evaluator = new DoubleEvaluator();

		//String javaForm = util.toJavaForm("ClearAll(x,y)");
		//System.out.println(javaForm.toString());
		try
		{

			IAST function = Plus(Times(C9,Slot(1)),Times(CN6,Slot(2)),Sqr(Slot(1)),Times(CN1,Slot(1),Slot(2)),Sqr(Slot(2)),ZZ(20L));
			println(function);
			println(util.eval(findRoots(function)));
			Slot.clear(util.getEvalEngine());

			function = Plus(Times(Slot(1), Slot(1)), Times(Plus(Slot(2), CN1), Plus(Slot(2), CN1)) );
			println("");
			println(function);
			println(util.eval(findRoots(function)));
			Slot.clear(util.getEvalEngine());

			function = Plus(Times(Slot(1), Slot(1)), CN1 );
			println("");
			println(function);
			println(util.eval(findRoots(function)));
			Slot.clear(util.getEvalEngine());

			function = Plus(Times(Slot(1), Slot(1)), CN1 );
			println("");
			println(function);
			println(util.eval(findRoots(function)));
			Slot.clear(util.getEvalEngine());

			function = Plus(Power(Slot(1), C4), Negate(Times(Slot(1), C3)), Times(C3,Power(Slot(1), C3)), C2);
			println("");
			println(function);
			println(util.eval(findRoots(function)));
			Slot.clear(util.getEvalEngine());

			function = Plus(Times(C9,Slot(1)),Times(CN6,Slot(2)),Sqr(Slot(1)),Times(CN1,Slot(1),Slot(2)),Sqr(Slot(2)),ZZ(20L));
			println("");
			println(function);
			println(util.eval(findRoots(function)));
			Slot.clear(util.getEvalEngine());
		}
		catch(Throwable e)
		{
			println(e.toString());
		};

	}
}
