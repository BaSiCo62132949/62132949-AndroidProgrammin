package ntu.basico.ontap1;

import java.io.Serializable; // Import để có thể gửi object qua Intent (cho phần nâng cao)

// Implement Serializable để có thể gửi đối tượng này qua Intent
public class MonAn implements Serializable {
    private String tenMonAn;
    private String moTa;
    private int hinhAnhResource; // Lưu ID của ảnh trong drawable

    // Constructor
    public MonAn(String tenMonAn, String moTa, int hinhAnhResource) {
        this.tenMonAn = tenMonAn;
        this.moTa = moTa;
        this.hinhAnhResource = hinhAnhResource;
    }

    // Getters
    public String getTenMonAn() {
        return tenMonAn;
    }

    public String getMoTa() {
        return moTa;
    }

    public int getHinhAnhResource() {
        return hinhAnhResource;
    }

    // Setters (không bắt buộc nhưng có thể hữu ích)
    public void setTenMonAn(String tenMonAn) {
        this.tenMonAn = tenMonAn;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public void setHinhAnhResource(int hinhAnhResource) {
        this.hinhAnhResource = hinhAnhResource;
    }
}