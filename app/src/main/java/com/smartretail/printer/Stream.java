//
//  Android PDF Writer
//  http://coderesearchlabs.com/androidpdfwriter
//
//  by Javier Santo Domingo (j-a-s-d@coderesearchlabs.com)
//

package com.smartretail.printer;

public class Stream extends EnclosedContent {

    public Stream() {
        super("stream\n", "endstream\n");
    }

}
