package com.mikeldpl.uifxconsole;

public class Test  {
    public static void main(String[] args) throws InterruptedException {
        UiFxConsole.configure().onClose(() -> System.out.println("oooo")).title("test TITLE").start();
        System.out.println("Asdasd");
        System.out.println("Asdasd2");
        Thread.sleep(3000);
    }
}
