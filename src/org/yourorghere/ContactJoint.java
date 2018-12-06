package org.yourorghere;

public class ContactJoint {

    //коэффициент "вытаскивани€" пересЄкшихс€ объектов
    private static final double ERP = 0.2d;
    //скокорость отдалени€ объектов (если упругость <> 0)
    private final double dstVelocity;
    //скорость отдалени€ дл€ решени€ проникновений
    private final double dstDisVelocity;
    //нормали дл€ решени€ отсокока и трени€
    private final Vector bounceNormal, frictionNormal;

    public ContactJoint(Vector normal, double deep) {
        //скорость отскока
        dstVelocity = Solver.E * Vector.getDP(Solver.BALL.velocity, normal);
        //скорость "вытаскивани€"
        dstDisVelocity = -ERP * Math.max(0d, deep - 0.05d);
        this.bounceNormal = normal;
        //нормаль трени€ - нормированнна€ скорость
        this.frictionNormal = Solver.BALL.velocity.getNormalized();
    }

    public void solveImpulse() {
        //вычис€лем вертикальную составл€ющую импульса
        double blambda = calcLambda(Solver.BALL.velocity, bounceNormal, dstVelocity);
        //если она < 0 - объекты будут слипатьс€
        if (blambda < 0d) {
            return;
        }
        //увеличиваем скорость шара на вертикальную составл€ющую
        applyImpulse(Solver.BALL.velocity, bounceNormal, blambda);
        //вычисл€ем горизонтальную состав€лющую
        double flambda = calcLambda(Solver.BALL.velocity, frictionNormal, 0);
        //трение не может быть больше реакции опоры на коэффициент трени€
        if (Math.abs(flambda) > (blambda * Solver.F)) {
            //урезаем трение до реации опоры с сохранением знака
            flambda = flambda > 0.0f ? 1.0f : -1.0f * blambda * Solver.F;
        }
        //увеличиваем скорость на горизонтальную составл€ющую
        applyImpulse(Solver.BALL.velocity, frictionNormal, flambda);
    }

    public void solvePenetration() {
        //горизонтальна€ составл€юща€ "вытаскивани€"
        double lambda = calcLambda(Solver.BALL.pvelocity, bounceNormal, dstDisVelocity);
        //если она < 0 - объекты будут слипатьс€
        if (lambda < 0d) {
            return;
        }
        //увеличиваем псевдоскорость шара на вертикальную составл€ющую    
        applyImpulse(Solver.BALL.pvelocity, bounceNormal, lambda);
    }

    private double calcLambda(Vector vel, Vector normal, double dstV) {
        //считаем необходимый испульс
        double dV = 0;
        //проекци€ скорости на нормаль
        dV -= Vector.getDP(vel, normal);
        //скорость отдалени€
        dV -= dstV;
        return dV;
    }

    //добавление импульса к скорост€м
    private void applyImpulse(Vector vel, Vector normal, double lambda) {
        vel.add(normal, lambda);
    }

}
