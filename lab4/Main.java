package sample;

import javax.media.j3d.*;
import javax.vecmath.*;
import com.sun.j3d.utils.geometry.*;
import com.sun.j3d.utils.universe.*;
import com.sun.j3d.utils.behaviors.vp.*;
import javax.swing.JFrame;



public class Main extends JFrame{

    public Canvas3D myCanvas3D;
    BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE);


    private final float legHeight = 1.0f;
    private final float legThickness = 0.1f;
    private final float seatSize = 0.8f;
    private final float seatThickness = 0.2f;
    private final float backRadius = 0.4f;
    private final float backThickness = 0.2f;

    private final float leg_away = seatSize * 2;
    private final float cubeSize = seatSize / 16;
    private final long todotime = 8000;

    private final int distance = 1;

    private int startingTime = 3000;


    public Main(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        myCanvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());

        SimpleUniverse simpUniv = new SimpleUniverse(myCanvas3D);
        simpUniv.getViewingPlatform().setNominalViewingTransform();
        createSceneGraph(simpUniv);
        addLight(simpUniv);


        OrbitBehavior ob = new OrbitBehavior(myCanvas3D);
        ob.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0),Double.MAX_VALUE));
        simpUniv.getViewingPlatform().setViewPlatformBehavior(ob);


        setTitle("Lab 4");
        setSize(700,700);
        getContentPane().add("Center", myCanvas3D);
        setVisible(true);
    }



    public void createSceneGraph(SimpleUniverse su)  {

        Background bg = new Background(new Color3f(1.0f,1.0f,1.0f));
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0),1000.0);
        bg.setApplicationBounds(bounds);

        BranchGroup theScene = new BranchGroup();
        theScene.addChild(this.initChair());
        theScene.addChild(bg);
        theScene.compile();
        su.addBranchGraph(theScene);
    }


    public TransformGroup initChair(){

        Color3f ambientColour = new Color3f(0.2f, 0.2f, 0.2f);
        Color3f emissiveColour = new Color3f(0.1f, 0.1f, 0.1f);
        Color3f diffuseColour = new Color3f(0.3f, 0.3f, 0.3f);
        Color3f specularColour = new Color3f(0.4f, 0.4f, 0.4f);
        float shininess = 100.0f;

        Appearance app = new Appearance();
        app.setMaterial(new Material(ambientColour, emissiveColour, diffuseColour,
                specularColour, shininess));


        ColorCube colorCube = new ColorCube(cubeSize);

        Transform3D tfColorCube = new Transform3D();
        tfColorCube.rotX(Math.PI/4);
        Transform3D rotation_z = new Transform3D();
        rotation_z.rotZ(Math.PI/4);
        tfColorCube.mul(rotation_z);
        TransformGroup tgColorCube = new TransformGroup(tfColorCube);
        tgColorCube.addChild(colorCube);



        TransformGroup tgmCube = new TransformGroup();
        tgmCube.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        Transform3D cubeRotationAxis = new Transform3D();
        cubeRotationAxis.rotY(Math.PI/2);
        Alpha cubeRotationAlpha = new Alpha(-1,
                Alpha.INCREASING_ENABLE,
                0,
                0,
                todotime,
                0,0,0,0,0);
        RotationInterpolator rotCube = new RotationInterpolator(
                cubeRotationAlpha,tgmCube,
                cubeRotationAxis,0.0f,(float) Math.PI*2);
        rotCube.setSchedulingBounds(bounds);
        tgmCube.addChild(tgColorCube);
        tgmCube.addChild(rotCube);


        Transform3D tfCube = new Transform3D();
        tfCube.setTranslation(new Vector3d(0,seatThickness-0.01,0));

        TransformGroup tgCube = new TransformGroup(tfCube);
        tgCube.addChild(tgmCube);



        Box seat = new Box(seatSize/2,seatThickness/2,seatSize/2,app);

        TransformGroup tgmSeat = new TransformGroup();
        tgmSeat.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        Transform3D seatAxis = new Transform3D();
        seatAxis.rotZ(Math.PI/2);
        Alpha seatAlpha = new Alpha(1,
                Alpha.INCREASING_ENABLE,
                0,
                startingTime,
                todotime,
                0,0,0,0,0);
        PositionInterpolator movSeat = new PositionInterpolator(
                seatAlpha,tgmSeat,seatAxis,0.0f,legHeight);
        movSeat.setSchedulingBounds(bounds);

        tgmSeat.addChild(seat);
        tgmSeat.addChild(tgCube);
        tgmSeat.addChild(movSeat);



        Transform3D tfSeat = new Transform3D();
        tfSeat.setTranslation(new Vector3d(0,-legHeight,0));

        TransformGroup tgSeat = new TransformGroup(tfSeat);
        tgSeat.addChild(tgmSeat);







        Cylinder back = new Cylinder(backRadius,backThickness,app);

        TransformGroup tgmBack = new TransformGroup();
        tgmBack.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        Transform3D backAxis = new Transform3D();
        backAxis.rotY(-Math.PI/2);
        Alpha backAlpha = new Alpha(1,
                Alpha.INCREASING_ENABLE,
                0,
                startingTime,
                todotime,
                0,0,0,0,0);
        PositionInterpolator movBack = new PositionInterpolator(
                backAlpha,tgmBack,backAxis,0.0f,legHeight);
        movBack.setSchedulingBounds(bounds);
        tgmBack.addChild(back);
        tgmBack.addChild(movBack);

        Transform3D tfBack = new Transform3D();
        tfBack.setTranslation(new Vector3d(0,backRadius+(seatThickness/2)+legHeight,
                -(seatSize/2)+(backThickness/2)));
        Transform3D rotationX = new Transform3D();
        rotationX.rotX(Math.PI/2);
        tfBack.mul(rotationX);
        TransformGroup tgBack = new TransformGroup(tfBack);
        tgBack.addChild(tgmBack);






        Alpha legAlpha = new Alpha(1,
                Alpha.INCREASING_ENABLE,
                0,
                startingTime,
                todotime,
                0,0,0,0,0);
        Transform3D y_axis = new Transform3D();



        Box flLeg = new Box(legThickness/2,legHeight/2,legThickness/2,app);

        TransformGroup tgmFLLeg = new TransformGroup();
        tgmFLLeg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        PositionInterpolator movflLeg = new PositionInterpolator(
                legAlpha,tgmFLLeg,y_axis,0.0f,leg_away);
        movflLeg.setSchedulingBounds(bounds);

        tgmFLLeg.addChild(flLeg);
        tgmFLLeg.addChild(movflLeg);

        Transform3D tfFLLeg = new Transform3D();
        tfFLLeg.setTranslation(new Vector3d(-(seatSize/2)+(legThickness/2) - leg_away,
                -(legHeight/2)-(seatThickness)/2,
                (seatSize/2)-(legThickness/2)));
        TransformGroup tgFLLeg = new TransformGroup(tfFLLeg);
        tgFLLeg.addChild(tgmFLLeg);





        Box frLeg = new Box(legThickness/2,legHeight/2,legThickness/2,app);

        TransformGroup tgmFRLeg = new TransformGroup();
        tgmFRLeg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        PositionInterpolator movfrLeg = new PositionInterpolator(
                legAlpha,tgmFRLeg,y_axis,0f,-leg_away);
        movfrLeg.setSchedulingBounds(bounds);

        tgmFRLeg.addChild(frLeg);
        tgmFRLeg.addChild(movfrLeg);

        Transform3D tfFRLeg = new Transform3D();
        tfFRLeg.setTranslation(new Vector3d((seatSize/2)-(legThickness/2) + leg_away,
                -(legHeight/2)-(seatThickness)/2,
                (seatSize/2)-(legThickness/2)));
        TransformGroup tgFRLeg = new TransformGroup(tfFRLeg);
        tgFRLeg.addChild(tgmFRLeg);




        Box blLeg = new Box(legThickness/2,legHeight/2,legThickness/2,app);


        TransformGroup tgmBLLeg = new TransformGroup();
        tgmBLLeg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        PositionInterpolator movBLLeg = new PositionInterpolator(
                legAlpha,tgmBLLeg,y_axis,0f,leg_away);
        movBLLeg.setSchedulingBounds(bounds);

        tgmBLLeg.addChild(blLeg);
        tgmBLLeg.addChild(movBLLeg);


        Transform3D tfBLLeg = new Transform3D();
        tfBLLeg.setTranslation(new Vector3d(-(seatSize/2)+(legThickness/2) - leg_away,
                -(legHeight/2)-(seatThickness)/2,
                -(seatSize/2)+(legThickness/2)));
        TransformGroup tgBLLeg = new TransformGroup(tfBLLeg);
        tgBLLeg.addChild(tgmBLLeg);







        Box brLeg = new Box(legThickness/2,legHeight/2,legThickness/2,app);

        TransformGroup tgmBRLeg = new TransformGroup();
        tgmBRLeg.setCapability(TransformGroup.ALLOW_TRANSFORM_WRITE);

        PositionInterpolator movBRLeg = new PositionInterpolator(
                legAlpha,tgmBRLeg,y_axis,0f,-leg_away);
        movBRLeg.setSchedulingBounds(bounds);

        tgmBRLeg.addChild(brLeg);
        tgmBRLeg.addChild(movBRLeg);

        Transform3D tfBRLeg = new Transform3D();
        tfBRLeg.setTranslation(new Vector3d((seatSize/2)-(legThickness/2)+leg_away,
                -(legHeight/2)-(seatThickness)/2,
                -(seatSize/2)+(legThickness/2)));
        TransformGroup tgBRLeg = new TransformGroup(tfBRLeg);
        tgBRLeg.addChild(tgmBRLeg);



        Transform3D tfChair = new Transform3D();
        tfChair.setTranslation(new Vector3d(0,0,-distance));
        TransformGroup tgChair = new TransformGroup(tfChair);
        tgChair.addChild(tgSeat);
        tgChair.addChild(tgBack);
        tgChair.addChild(tgFLLeg);
        tgChair.addChild(tgFRLeg);
        tgChair.addChild(tgBLLeg);
        tgChair.addChild(tgBRLeg);
        return tgChair;
    }




    public void addLight(SimpleUniverse su){
        BranchGroup bgLight = new BranchGroup();
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
        Color3f lightColour1 = new Color3f(1.0f,1.0f,1.0f);
        Vector3f lightDir1  = new Vector3f(-1.0f,0.0f,-0.5f);
        DirectionalLight light1 = new DirectionalLight(lightColour1, lightDir1);
        light1.setInfluencingBounds(bounds);
        bgLight.addChild(light1);

        Vector3f lightDir2  = new Vector3f(1.0f,0.0f,0.5f);
        DirectionalLight light2 = new DirectionalLight(lightColour1, lightDir2);
        light2.setInfluencingBounds(bounds);

        bgLight.addChild(light2);
        su.addBranchGraph(bgLight);
    }

    public static void main(String[] args)  {
        new Main();
    }

}
