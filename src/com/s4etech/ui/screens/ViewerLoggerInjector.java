package com.s4etech.ui.screens;
import net.bytebuddy.agent.ByteBuddyAgent;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassReloadingStrategy;
import net.bytebuddy.matcher.ElementMatchers;

public class ViewerLoggerInjector {

    public static void instrument() {
        ByteBuddyAgent.install(); // Instala o agente de instrumentação

        new ByteBuddy()
            .redefine(Viewer.class)
            .visit(Advice.to(LogAdvice.class).on(ElementMatchers.isMethod()))
            .make()
            .load(Viewer.class.getClassLoader(), ClassReloadingStrategy.fromInstalledAgent());
    }

    public static class LogAdvice {
        @Advice.OnMethodEnter
        public static void onEnter(@Advice.Origin String method) {
            System.out.println("→ Entrando no método: " + method);
        }

        @Advice.OnMethodExit
        public static void onExit(@Advice.Origin String method) {
            System.out.println("← Saindo do método: " + method);
        }
    }
}
