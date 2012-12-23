public class Animation {
  public int id;
	public String name;
	public Object3D object;
	public Light3D light;
	public Animation parent;
	public float[] pivot;

	public AnimKey[] position;
	public AnimKey[] rotation;
	public AnimKey[] scaling;

	public float[] result;
	public float[] world;
}
