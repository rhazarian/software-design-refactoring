package ru.akirakozov.sd.refactoring.utility;

import ru.akirakozov.sd.refactoring.domain.Product;

public class ResponseBuilder {
    private static final String SEPARATOR = System.getProperty("line.separator");
    private static final String HEADER = "<html><body>" + SEPARATOR;
    private static final String FOOTER = "</body></html>";

    private final StringBuilder builder = new StringBuilder();

    public ResponseBuilder() {
        builder.append(HEADER);
    }

    public void append(final String str) {
        builder.append(str).append(SEPARATOR);
    }

    public void append(final int i) {
        builder.append(i).append(SEPARATOR);
    }

    public void append(final long lng) {
        builder.append(lng).append(SEPARATOR);
    }

    public void append(final Product product) {
        builder.append(product.getName()).append("\t").append(product.getPrice()).append("</br>").append(SEPARATOR);
    }

    public String toString() {
        return builder.toString() + FOOTER;
    }
}
