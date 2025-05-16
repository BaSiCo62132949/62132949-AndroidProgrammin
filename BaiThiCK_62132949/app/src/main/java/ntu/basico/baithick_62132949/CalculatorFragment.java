package ntu.basico.baithick_62132949;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.mariuszgromada.math.mxparser.*;
import java.text.NumberFormat;
import java.util.Locale;

public class CalculatorFragment extends Fragment implements View.OnClickListener {

    private static final String TAG_CALC = "MayTinhFragment"; // Đổi tag cho dễ lọc

    private TextView textPhepTinh;
    private TextView textKetQua;

    private StringBuilder bieuThucHienTai = new StringBuilder();
    private boolean soCuoiCungLaSo; // True nếu ký tự cuối được thêm là số hoặc dấu đóng ngoặc ')' hoặc '%'
    private boolean trangThaiLoi;
    private boolean soCuoiCungLaDauCham;
    private int soNgoacMo = 0; // Đếm số lượng ngoặc mở chưa được đóng
    private TextView tieuDeMayTinh; // Không cần thiết nếu chỉ hiển thị text cố định
    private ImageButton nutLuuLichSuTinhToan;
    private HistoryDbHelper dbHelperMayTinh;
    private String bieuThucCuoiCungTruocKhiBang = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calculator, container, false);

        textPhepTinh = view.findViewById(R.id.textPhepTinh);
        textKetQua = view.findViewById(R.id.textKetQua);
        nutLuuLichSuTinhToan = view.findViewById(R.id.nutLuuLichSuTinhToan);
        dbHelperMayTinh = new HistoryDbHelper(requireContext());

        ganSuKienChoNutSo(view);
        ganSuKienChoNutPhepToan(view);
        ganSuKienChoNutLuu();



        xoaManHinh(); // Khởi tạo màn hình sạch
        return view;
    }

    private void ganSuKienChoNutSo(View viewCha) {
        int[] cacNutSo = {R.id.nut0, R.id.nut1, R.id.nut2, R.id.nut3,
                R.id.nut4, R.id.nut5, R.id.nut6, R.id.nut7, R.id.nut8, R.id.nut9,
                R.id.nutThapPhan};
        for (int idNut : cacNutSo) {
            if (viewCha.findViewById(idNut) != null) {
                viewCha.findViewById(idNut).setOnClickListener(this);
            }
        }
    }

    private void ganSuKienChoNutPhepToan(View viewCha) {
        int[] cacNutPhepToan = {R.id.nutCong, R.id.nutTru, R.id.nutNhan, R.id.nutChia,
                R.id.nutPhanTram, R.id.nutNgoac, R.id.nutXoaHet, R.id.nutBang};
        for (int idNut : cacNutPhepToan) {
            if (viewCha.findViewById(idNut) != null) {
                viewCha.findViewById(idNut).setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        Button nutDuocNhan = (Button) v;
        String kyTuNut = nutDuocNhan.getText().toString();
        int idCuaNut = v.getId();

        if (trangThaiLoi && idCuaNut != R.id.nutXoaHet) {
            return;
        }

        if (idCuaNut == R.id.nut0 || idCuaNut == R.id.nut1 || idCuaNut == R.id.nut2 ||
                idCuaNut == R.id.nut3 || idCuaNut == R.id.nut4 || idCuaNut == R.id.nut5 ||
                idCuaNut == R.id.nut6 || idCuaNut == R.id.nut7 || idCuaNut == R.id.nut8 ||
                idCuaNut == R.id.nut9 || idCuaNut == R.id.nutThapPhan) {
            themSoHoacDauThapPhan(kyTuNut);
        } else if (idCuaNut == R.id.nutCong || idCuaNut == R.id.nutTru ||
                idCuaNut == R.id.nutNhan || idCuaNut == R.id.nutChia) {
            themPhepToan(kyTuNut);
        } else if (idCuaNut == R.id.nutNgoac) {
            xuLyNgoac();
        } else if (idCuaNut == R.id.nutPhanTram) {
            xuLyPhanTram();
        } else if (idCuaNut == R.id.nutXoaHet) {
            xoaManHinh();
        } else if (idCuaNut == R.id.nutBang) {
            tinhKetQua();
        }
        // else if (idCuaNut == R.id.nutXoaLui) { // Nếu bạn có nút xóa lùi
        //     xuLyXoaLui();
        // }
        capNhatManHinhPhepTinh();
    }

    private void capNhatManHinhPhepTinh() {
        textPhepTinh.setText(bieuThucHienTai.toString());
    }

    private void themSoHoacDauThapPhan(String kyTu) {
        if (trangThaiLoi) return;

        if (kyTu.equals(".")) {
            // Chỉ cho phép một dấu chấm trong một số hiện tại và sau một số
            if (soCuoiCungLaSo && !soCuoiCungLaDauCham) {
                bieuThucHienTai.append(kyTu);
                soCuoiCungLaDauCham = true;
                soCuoiCungLaSo = false; // Sau dấu chấm không phải là "số hoàn chỉnh" ngay
            }
        } else { // Nếu là số
            // Nếu ký tự cuối là ')' hoặc '%', thêm toán tử nhân ngầm định (hoặc báo lỗi tùy thiết kế)
            char kyTuCuoi = (bieuThucHienTai.length() > 0) ? bieuThucHienTai.charAt(bieuThucHienTai.length() - 1) : ' ';
            if (kyTuCuoi == ')' || kyTuCuoi == '%') {
                bieuThucHienTai.append("×"); // Hoặc '*' nếu mxparser dùng '*'
                soCuoiCungLaSo = false;
                soCuoiCungLaDauCham = false;
            }
            bieuThucHienTai.append(kyTu);
            soCuoiCungLaSo = true;
        }
    }

    private void themPhepToan(String phepToan) {
        if (trangThaiLoi) return;

        if (bieuThucHienTai.length() > 0) {
            char kyTuCuoi = bieuThucHienTai.charAt(bieuThucHienTai.length() - 1);
            // Chỉ thêm toán tử nếu ký tự cuối là số, hoặc đóng ngoặc, hoặc %
            if (Character.isDigit(kyTuCuoi) || kyTuCuoi == ')' || kyTuCuoi == '%' || kyTuCuoi == '.') {
                bieuThucHienTai.append(phepToan);
                soCuoiCungLaSo = false;
                soCuoiCungLaDauCham = false;
            }
            // Hoặc nếu ký tự cuối là toán tử khác, thì thay thế nó (trừ khi là dấu trừ âm)
            else if (laPhepToan(kyTuCuoi) && !(phepToan.equals("−") && kyTuCuoi == '(' )) { // Cho phép nhập số âm sau dấu (
                // Nếu không phải nhập số âm sau mở ngoặc, thì thay thế toán tử cũ
                if (! ( (kyTuCuoi == '×' || kyTuCuoi == '÷') && phepToan.equals("−") ) ){ // Cho phép 5*-2
                    bieuThucHienTai.setLength(bieuThucHienTai.length() - 1);
                }
                bieuThucHienTai.append(phepToan);
                soCuoiCungLaSo = false;
                soCuoiCungLaDauCham = false;
            }
        } else if (phepToan.equals("−")) { // Cho phép nhập số âm ở đầu
            bieuThucHienTai.append(phepToan);
            soCuoiCungLaSo = false;
            soCuoiCungLaDauCham = false;
        }
    }

    private void xuLyNgoac() {
        if (trangThaiLoi) return;

        char kyTuCuoi = (bieuThucHienTai.length() > 0) ? bieuThucHienTai.charAt(bieuThucHienTai.length() - 1) : ' ';

        // Ưu tiên đóng ngoặc nếu có ngoặc mở và ký tự cuối cho phép đóng ngoặc
        if (soNgoacMo > 0 && (Character.isDigit(kyTuCuoi) || kyTuCuoi == ')' || kyTuCuoi == '%')) {
            bieuThucHienTai.append(")");
            soNgoacMo--;
            soCuoiCungLaSo = true; // Coi như kết thúc một "số" hoặc biểu thức con
        }
        // Ngược lại, mở ngoặc
        else {
            // Nếu ký tự cuối là số hoặc dấu đóng ngoặc, thêm dấu nhân ngầm
            if (Character.isDigit(kyTuCuoi) || kyTuCuoi == ')' || kyTuCuoi == '%') {
                bieuThucHienTai.append("×"); // Hoặc '*'
                soCuoiCungLaSo = false;
            }
            bieuThucHienTai.append("(");
            soNgoacMo++;
            soCuoiCungLaSo = false; // Sau khi mở ngoặc, không phải là số
        }
        soCuoiCungLaDauCham = false;
    }

    private void xuLyPhanTram() {
        if (trangThaiLoi) return;

        if (bieuThucHienTai.length() > 0) {
            char kyTuCuoi = bieuThucHienTai.charAt(bieuThucHienTai.length() - 1);
            // Chỉ thêm % nếu ký tự cuối là số hoặc dấu đóng ngoặc
            if (Character.isDigit(kyTuCuoi) || kyTuCuoi == ')') {
                bieuThucHienTai.append("%");
                soCuoiCungLaSo = true; // Sau % có thể coi là một "giá trị"
                soCuoiCungLaDauCham = false;
            } else {
                Toast.makeText(getContext(), "Không hợp lệ để tính %", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(getContext(), "Nhập số trước khi tính %", Toast.LENGTH_SHORT).show();
        }
    }

    private void xoaManHinh() {
        bieuThucCuoiCungTruocKhiBang = "";
        bieuThucHienTai.setLength(0);
        textPhepTinh.setText("");
        textKetQua.setText("");
        soCuoiCungLaSo = false;
        trangThaiLoi = false;
        soCuoiCungLaDauCham = false;
        soNgoacMo = 0;
        Log.d(TAG_CALC, "Màn hình đã xóa");
    }

    private void tinhKetQua() {
        if (trangThaiLoi) { // Nếu đang lỗi, chỉ cho phép xóa
            xoaManHinh();
            return;
        }
        // Đóng tất cả các ngoặc mở còn lại nếu người dùng không tự đóng
        while (soNgoacMo > 0) {
            bieuThucHienTai.append(")");
            soNgoacMo--;
        }
        capNhatManHinhPhepTinh(); // Cập nhật lại biểu thức với các dấu ngoặc đã đóng

        if (bieuThucHienTai.length() > 0) {
            // Thay thế ký tự hiển thị bằng ký tự mxparser hiểu
            String bieuThucDeTinh = bieuThucHienTai.toString()
                    .replace('×', '*')
                    .replace('÷', '/')
                    .replace('−', '-'); // Đảm bảo ký tự này khớp với nút bạn dùng

            Log.d(TAG_CALC, "Biểu thức chuẩn bị tính: " + bieuThucDeTinh);

            Expression e = new Expression(bieuThucDeTinh);
            double ketQuaTinhDuoc = e.calculate();

            if (Double.isNaN(ketQuaTinhDuoc) || Double.isInfinite(ketQuaTinhDuoc)) {
                textKetQua.setText("Lỗi");
                trangThaiLoi = true;
                Log.e(TAG_CALC, "Lỗi tính toán hoặc NaN/Infinite cho: " + bieuThucDeTinh);
            } else {
                NumberFormat nf = NumberFormat.getNumberInstance(Locale.US);
                nf.setMaximumFractionDigits(10); // Giới hạn số chữ số thập phân
                nf.setGroupingUsed(false); // Không dùng dấu phân cách cho kết quả thô

                String ketQuaFormatted = nf.format(ketQuaTinhDuoc);
                // Loại bỏ .0 không cần thiết
                if (ketQuaFormatted.endsWith(".0")) {
                    ketQuaFormatted = ketQuaFormatted.substring(0, ketQuaFormatted.length() - 2);
                }
                textKetQua.setText(ketQuaFormatted);
                Log.d(TAG_CALC, "Kết quả tính toán: " + ketQuaFormatted);

                // Reset cho phép tính mới dựa trên kết quả
                bieuThucCuoiCungTruocKhiBang = bieuThucHienTai.toString();
                bieuThucHienTai.setLength(0);
                bieuThucHienTai.append(ketQuaFormatted);
                soCuoiCungLaSo = true;
                soCuoiCungLaDauCham = ketQuaFormatted.contains(".");
                trangThaiLoi = false;
                soNgoacMo = 0; // Reset số ngoặc mở
                // capNhatManHinhPhepTinh(); // Cập nhật lại ô biểu thức với kết quả
            }
        } else {
            textKetQua.setText("0");
            trangThaiLoi = true;
            bieuThucCuoiCungTruocKhiBang = bieuThucHienTai.toString();
        }
    }

    private boolean laPhepToan(char c) {
        return c == '+' || c == '−' || c == '×' || c == '÷';
    }


     private void xuLyXoaLui() {
         if (trangThaiLoi) {
             xoaManHinh();
             return;
         }
         if (bieuThucHienTai.length() > 0) {
             char kyTuBiXoa = bieuThucHienTai.charAt(bieuThucHienTai.length() - 1);
             bieuThucHienTai.setLength(bieuThucHienTai.length() - 1);

             if (kyTuBiXoa == '(') soNgoacMo--;
             else if (kyTuBiXoa == ')') soNgoacMo++; // Nếu xóa nhầm dấu đóng, số ngoặc mở tăng lại

             if (bieuThucHienTai.length() > 0) {
                 char kyTuCuoiMoi = bieuThucHienTai.charAt(bieuThucHienTai.length() - 1);
                 soCuoiCungLaSo = Character.isDigit(kyTuCuoiMoi) || kyTuCuoiMoi == ')' || kyTuCuoiMoi == '%';
                 soCuoiCungLaDauCham = false; // Cần logic phức tạp hơn để xác định lại lastDot
                 // Tạm thời reset, hoặc tìm dấu chấm cuối cùng
                 for (int i = bieuThucHienTai.length() - 1; i >= 0; i--) {
                     if (bieuThucHienTai.charAt(i) == '.') {
                         soCuoiCungLaDauCham = true;
                         break;
                     }
                     if (laPhepToan(bieuThucHienTai.charAt(i)) || bieuThucHienTai.charAt(i) == '(') {
                         break; // Đã qua số hiện tại
                     }
                 }
             } else {
                 soCuoiCungLaSo = false;
                 soCuoiCungLaDauCham = false;
             }
         }
         capNhatManHinhPhepTinh();
         textKetQua.setText(""); // Xóa kết quả cũ khi sửa biểu thức
     }
    private void ganSuKienChoNutLuu() {
        nutLuuLichSuTinhToan.setOnClickListener(v -> {
            luuPhepTinhVaoLichSu();
        });
    }

    private void luuPhepTinhVaoLichSu() {
        String phepTinhDeLuu = bieuThucCuoiCungTruocKhiBang;
        String ketQua = textKetQua.getText().toString();
        Log.d(TAG_CALC, "Attempting to save: Expression='" + phepTinhDeLuu + "', Result='" + ketQua + "'");

        // Chỉ lưu nếu có phép tính và kết quả hợp lệ (không phải "Lỗi" hoặc trống)
        if (!TextUtils.isEmpty(phepTinhDeLuu) && !TextUtils.isEmpty(ketQua) &&
                !ketQua.equals("Lỗi") && !ketQua.equals("Cần thư viện tính toán") &&
                !phepTinhDeLuu.equals(ketQua)) { // Tránh lưu khi chỉ có kết quả từ phép tính trước

            Log.d(TAG_CALC, "Save condition MET. Proceeding to save.");
            // Loại lịch sử có thể là "Máy tính" hoặc một tên khác
            String loaiChuyenDoi = "Phép tính"; // Hoặc getString(R.string.history_type_calculation)

            boolean thanhCong = dbHelperMayTinh.insertHistory(loaiChuyenDoi, phepTinhDeLuu, "=", ketQua, "");

            if (thanhCong) {
                Toast.makeText(getContext(), "Đã lưu phép tính vào lịch sử", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Lỗi khi lưu lịch sử phép tính", Toast.LENGTH_SHORT).show();
            }
            bieuThucCuoiCungTruocKhiBang = "";
        } else {
            Log.e(TAG_CALC, "Save condition FAILED. Reasons:");
            if (TextUtils.isEmpty(phepTinhDeLuu)) Log.e(TAG_CALC, " - phepTinh is empty.");
            if (TextUtils.isEmpty(ketQua)) Log.e(TAG_CALC, " - ketQua is empty.");
            if (ketQua.equals("Lỗi")) Log.e(TAG_CALC, " - ketQua is 'Lỗi'.");
            if (ketQua.equals("Cần thư viện tính toán")) Log.e(TAG_CALC, " - ketQua is 'Cần thư viện tính toán'.");
            if (phepTinhDeLuu.equals(ketQua)) Log.e(TAG_CALC, " - phepTinh ('" + phepTinhDeLuu + "') equals ketQua ('" + ketQua + "').");

            Toast.makeText(getContext(), "Không có phép tính hợp lệ để lưu", Toast.LENGTH_SHORT).show();
        }
    }
}