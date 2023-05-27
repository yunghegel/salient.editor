package backend;

import java.io.OutputStream;
import java.io.PrintStream;

public class OutputStreamInterceptor extends PrintStream {

    public OnPrintListener onPrintListener;

    public OutputStreamInterceptor(OutputStream out) {
        super(out);
        intercept();
    }

    void intercept(){
        //replace System.out with this

        System.setOut(this);
    }

    public void setOnPrintListener(OnPrintListener onPrintListener) {
        this.onPrintListener = onPrintListener;
    }

    @Override
    public void print(String s) {
        super.print(s);
        if (onPrintListener != null) {
            onPrintListener.onPrint(s);
        }
    }

    @Override
    public void println(String x) {
        super.println(x);
        if (onPrintListener != null) {
            onPrintListener.onPrint(x);
        }
    }

    public interface OnPrintListener {
        void onPrint(String s);
    }

}
