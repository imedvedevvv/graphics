package sample;

import javax.vecmath.*;

import com.sun.j3d.utils.image.TextureLoader;
import com.sun.j3d.utils.universe.*;
import javax.media.j3d.*;
import com.sun.j3d.utils.behaviors.vp.*;
import javax.swing.JFrame;
import com.sun.j3d.loaders.*;
import com.sun.j3d.loaders.objectfile.*;
import java.util.Map;


public class Main extends JFrame{
    public Canvas3D myCanvas3D;
    static Map<String, Shape3D> nameMap;

    static TransformGroup wholeCat;
    static Transform3D transform3D;

    public Main(){
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        myCanvas3D = new Canvas3D(SimpleUniverse.getPreferredConfiguration());
        SimpleUniverse simpUniv = new SimpleUniverse(myCanvas3D);
        simpUniv.getViewingPlatform().setNominalViewingTransform();

        // set the geometry and transformations
        createSceneGraph(simpUniv);
        addLight(simpUniv);

        OrbitBehavior ob = new OrbitBehavior(myCanvas3D);
        ob.setSchedulingBounds(new BoundingSphere(new Point3d(0.0,0.0,0.0), Double.MAX_VALUE));
        simpUniv.getViewingPlatform().setViewPlatformBehavior(ob);

        setTitle("Cat");
        setSize(700,700);
        getContentPane().add("Center", myCanvas3D);
        setVisible(true);
    }

    public void createSceneGraph(SimpleUniverse su){
        // loading object
        ObjectFile f = new ObjectFile(ObjectFile.RESIZE);
        BranchGroup catBranchGroup = new BranchGroup();
        TextureLoader t = new TextureLoader("D:\\IdeaProjects\\Lab5\\src\\sample\\source\\ufo.jpg", myCanvas3D);
        Background catBackground =  new Background(t.getImage());

        Scene catScene = null;
        try{
            catScene = f.load("D:\\IdeaProjects\\Lab5\\src\\sample\\source\\13051_ Russian_Blue_v2_l2.obj");
        }
        catch (Exception e){
            System.out.println("File loading failed:" + e);
        }
        nameMap = catScene.getNamedObjects();

        wholeCat = new TransformGroup();

        transform3D = new Transform3D();
        transform3D.rotX(-Math.PI / 2);
        wholeCat.setTransform(transform3D);
        transform3D.setTranslation(new Vector3f(0, -1.3f, 0));
        wholeCat.setTransform(transform3D);

        transform3D.rotX(-Math.PI / 2);
        wholeCat.setTransform(transform3D);

        // start animation
        Transform3D startTransformation = new Transform3D();
        startTransformation.rotX(-Math.PI / 2);
        startTransformation.setScale(1.0/6);
        Transform3D combinedStartTransformation = new Transform3D();
        combinedStartTransformation.mul(startTransformation);

        TransformGroup catStartTransformGroup = new TransformGroup(combinedStartTransformation);

        Appearance bodyApp = addAppearance();

        TransformGroup sceneGroup = new TransformGroup();
        TransformGroup tgBody = new TransformGroup();

        Shape3D catBodyShape = nameMap.get("russian_blue");
        catBodyShape.setAppearance(bodyApp);
        tgBody.addChild(catBodyShape.cloneTree());

        sceneGroup.addChild(tgBody.cloneTree());

        // movement from start position
        TransformGroup whiteTransXformGroup = translate(
                catStartTransformGroup,
                new Vector3f(0.0f,0.0f,0.5f));

        TransformGroup whiteRotXformGroup = rotate(whiteTransXformGroup, new Alpha(10,5000));
        catBranchGroup.addChild(whiteRotXformGroup);
        catStartTransformGroup.addChild(sceneGroup);

        // adding background to the branch group
        BoundingSphere bounds = new BoundingSphere(new Point3d(120.0,250.0,100.0),Double.MAX_VALUE);
        catBackground.setApplicationBounds(bounds);
        catBranchGroup.addChild(catBackground);

        catBranchGroup.compile();
        su.addBranchGraph(catBranchGroup);
    }

    public void addLight(SimpleUniverse su){
        // cat light
        BranchGroup bgLight = new BranchGroup();
        BoundingSphere bounds = new BoundingSphere(new Point3d(0.0,0.0,0.0), 100.0);
        Color3f lightColour1 = new Color3f(65/255f, 30/255f, 25/255f);
        Vector3f lightDir1 = new Vector3f(-1.0f,0.0f,-0.5f);
        DirectionalLight light1 = new DirectionalLight(lightColour1, lightDir1);
        light1.setInfluencingBounds(bounds);
        bgLight.addChild(light1);
        su.addBranchGraph(bgLight);
    }

    TransformGroup translate(Node node,Vector3f vector){
        Transform3D transform3D = new Transform3D();
        transform3D.setTranslation(vector);
        TransformGroup transformGroup =
                new TransformGroup();
        transformGroup.setTransform(transform3D);

        transformGroup.addChild(node);
        return transformGroup;
    }

    TransformGroup rotate(Node node,Alpha alpha){
        TransformGroup xformGroup = new TransformGroup();
        xformGroup.setCapability(
                TransformGroup.ALLOW_TRANSFORM_WRITE);

        // Create an interpolator (calculates coordinates) for rotating the node
        RotationInterpolator interpolator =
                new RotationInterpolator(alpha,xformGroup);

        // Set the animation region for this interpolator
        interpolator.setSchedulingBounds(new BoundingSphere(
                new Point3d(0.0,0.0,0.0),1.0));

        //Populate the xform group.
        xformGroup.addChild(interpolator);
        xformGroup.addChild(node);

        return xformGroup;
    }

    private Appearance addAppearance(){
        Appearance catAppearance = new Appearance();
        catAppearance.setTexture(getTexture("D:\\IdeaProjects\\Lab5\\src\\sample\\source\\Russian_Blue_dif.jpg"));
        TextureAttributes texAttr = new TextureAttributes();
        catAppearance.setTextureAttributes(texAttr);
        catAppearance.setMaterial(getMaterial());
        Shape3D plane = nameMap.get("russian_blue");
        plane.setAppearance(catAppearance);
        return catAppearance;
    }

    Texture getTexture(String path) {
        TextureLoader textureLoader = new TextureLoader(path,"LUMINANCE",myCanvas3D);
        Texture texture = textureLoader.getTexture();
        texture.setBoundaryModeS(Texture.WRAP);
        texture.setBoundaryModeT(Texture.WRAP);
        texture.setBoundaryColor( new Color4f( 0.0f, 1.0f, 0.0f, 0.0f ) );
        return texture;
    }

    Material getMaterial() {
        Material material = new Material();
        material.setAmbientColor ( new Color3f( 0.33f, 0.26f, 0.23f) );
        material.setDiffuseColor ( new Color3f( 0.50f, 0.11f, 0.2f ) );
        material.setSpecularColor( new Color3f( 0.95f, 0.73f, 0.1f ) );
        material.setShininess( 0.3f );
        material.setLightingEnable(true);
        return material;
    }

    public static void main(String[] args) {
        Main start = new Main();
    }

}
