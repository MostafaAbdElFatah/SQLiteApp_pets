package comq.mostafa.fci.pets.data;

public class ActivityListener {
    public interface CallBack {
        void onSavePet();
        void onUpdatePet();
        void onDeletePet();
    }
}
