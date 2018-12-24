package org.yourorghere;

public class ContactJoint {

    //коэффициент "вытаскивани€" пересЄкшихс€ объектов
    private static final double ERP = 0.2d;
    //скокорость отдалени€ объектов (если упругость <> 0)
    private final double dstVelocity;
    //скорость отдалени€ дл€ решени€ проникновений
    private final double dstDisVelocity;
    //инвертированна€ сумма инвертированнвх масс (так надо)
    private final double iSumiMass;
    //нормали дл€ решени€ отскока и трени€
    private final Vector bounceNormal;
    private final Vector frictionNormal;
    //два сталкивающихс€ объекта
    private final Moveable a, b;

    public ContactJoint(Moveable a, Moveable b, Vector normal, double deep) {
        this.a = a;
        this.b = b;
        dstVelocity = Solver.E * (Vector.getDP(a.velocity, normal) - Vector.getDP(b.velocity, normal));
        dstDisVelocity = -ERP * Math.max(0d, deep - 0.05d);
        iSumiMass = 1.0d / (a.iMass + b.iMass);
        this.bounceNormal = normal;
        this.frictionNormal = a.velocity.getNormalized();
    }

    public void solveImpulse() {
        //вычис€лем вертикальную составл€ющую импульса
        double bounce_lambda = calcLambda(a.velocity, b.velocity, bounceNormal, dstVelocity);
        //если она < 0 - объекты будут слипатьс€
        if (bounce_lambda < 0d) {
            return;
        }
        //увеличиваем скорость шара на вертикальную составл€ющую
        applyImpulse(a.velocity, b.velocity, bounceNormal, bounce_lambda);
        //вычисл€ем горизонтальную состав€лющую
        double friction_lambda = calcLambda(a.velocity, b.velocity, frictionNormal, 0);
        //трение не может быть больше реакции опоры на коэффициент трени€
        if (Math.abs(friction_lambda) > (bounce_lambda * Solver.F)) {
            //урезаем трение до реации опоры с сохранением знака
            friction_lambda = friction_lambda > 0.0f ? 1.0f : -1.0f * bounce_lambda * Solver.F;
        }
        //увеличиваем скорость на горизонтальную составл€ющую
        applyImpulse(a.velocity, b.velocity, frictionNormal, friction_lambda);
    }

    public void solvePenetration() {
        //горизонтальна€ составл€юща€ "вытаскивани€"
        double lambda = calcLambda(a.pvelocity, b.pvelocity, bounceNormal, dstDisVelocity);
        //если она < 0 - объекты будут слипатьс€
        if (lambda < 0d) {
            return;
        }
        //увеличиваем псевдоскорость шара на вертикальную составл€ющую    
        applyImpulse(a.pvelocity, b.pvelocity, bounceNormal, lambda);
    }

    private double calcLambda(Vector aVel, Vector bVel, Vector normal, double distanceVelocity) {
        //считаем необходимый импульс
        double dV = 0;
        //проекци€ каждой скорости на нормаль
        dV -= Vector.getDP(aVel, normal);
        dV += Vector.getDP(bVel, normal);
        //скорость отдалени€
        dV -= distanceVelocity;
        //делим на 2 т.к. объекта два
        return dV * iSumiMass;
    }

    //добавление импульса к скорост€м
    private void applyImpulse(Vector aVel, Vector bVel, Vector normal, double lambda) {
        aVel.add(normal, lambda * a.iMass);
        bVel.add(normal, -lambda * b.iMass);
    }

}
