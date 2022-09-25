package com.hayden.jsonparselibrary.decompile;

import org.jd.core.v1.ClassFileToJavaSourceDecompiler;

public class Decompile {

    DecompilePrinter printer;
    LoadClass loadClass;

    public Decompile(
            DecompilePrinter printer,
            LoadClass loadClass
    )
    {
        this.printer = printer;
        this.loadClass = loadClass;
    }

    public String decompile(String name)
    {

        var decompiler = new ClassFileToJavaSourceDecompiler();

        if (loadClass == null) {
            loadClass = new LoadClass();
            printer = new DecompilePrinter();
        }

        try {
            decompiler.decompile(
                    loadClass,
                    printer,
                    name
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
        return printer.toString();

    }

}
