# SonarQube Demo - Spring Boot

Project Spring Boot dùng để **demo SonarQube**, chứa cả **code tốt (Good)** và **code xấu (Bad)** để công cụ báo cáo và so sánh.

## Cấu trúc

### Code tốt (Good)

- **`/entity/Product.java`** – Entity có validation, `@PrePersist`
- **`/dto/ProductRequest.java`, ProductResponse.java`** – DTO với Bean Validation
- **`/repository/ProductRepository.java`** – JpaRepository + query an toàn (parameterized)
- **`/service/ProductService.java`** – Service có transaction, exception rõ ràng
- **`/controller/ProductController.java`** – REST API có `@Valid`, HTTP status chuẩn
- **`/exception/GlobalExceptionHandler.java`** – Xử lý lỗi tập trung, không để exception lộ ra ngoài

### Code xấu (Bad) – để SonarQube báo

| File | Vấn đề SonarQube thường báo |
|------|-----------------------------|
| **BadUserService.java** | SQL Injection (S2077), hardcoded credentials (S2068), empty catch (S108), NPE (S2259), `printStackTrace` (S1148) |
| **BadUtil.java** | Magic numbers (S109), code duplication, cognitive complexity cao, unused variable (S1481), string concat trong loop (S1643) |
| **BadReportController.java** | Endpoint dùng input không validate → SQL injection, raw type `Map` (S3740), `System.out` (S106) |
| **BadResourceLeak.java** | Stream/reader không đóng (S2095), `Random` dùng cho token (S2245), `printStackTrace` (S1148) |

## Chạy ứng dụng

```bash
./mvnw spring-boot:run
```

Hoặc với Maven đã cài:

```bash
mvn spring-boot:run
```

- API gọi thử: `http://localhost:8080/api/products`
- H2 Console: `http://localhost:8080/h2-console`

## Jenkins Pipeline (Cách A – Jenkinsfile trong repo)

### Bước 1: Cấu hình Jenkins

1. **Cài plugin**: Manage Jenkins → Plugins → cài **SonarQube Scanner** (và **Pipeline**, **Pipeline Maven** nếu chưa có).
2. **SonarQube server**: Manage Jenkins → System → SonarQube servers → Add SonarQube:
   - **Name**: `SonarQube`
   - **Server URL**: `http://sonarqube:9000` (khi Jenkins + SonarQube chạy cùng Docker network). Nếu Jenkins chạy trên host: `http://localhost:9000`
   - **Server authentication token**: Add → Kind = Secret text, Secret = token SonarQube (Global Analysis Token), ID = `sonarqube-token` → Save.
3. **Maven**: Manage Jenkins → Tools → Maven → Add Maven: tick "Install automatically", name = `Maven` (trùng với `tools { maven 'Maven' }` trong Jenkinsfile).

### Bước 2: Tạo Pipeline job

1. New Item → **Pipeline** → OK.
2. **Pipeline**:
   - **Definition**: Pipeline script from SCM
   - **SCM**: Git (nếu repo đã push lên Git) — Repository URL trỏ tới repo này; hoặc **Definition**: Pipeline script, dán nội dung từ `Jenkinsfile` (khi code chỉ ở local, copy/paste Jenkinsfile vào đây).
   - **Script Path**: `Jenkinsfile`
3. Save → **Build Now**.

Pipeline sẽ chạy: Checkout → Build → Test → SonarQube Analysis. Kết quả hiện trên SonarQube project **sonarqube-demo**.

### Code chỉ ở local (không có Git remote)

- Trong job: Definition chọn **Pipeline script**.
- Copy toàn bộ nội dung file `Jenkinsfile` vào ô Script.
- Ở mục **Pipeline** phía trên, chọn "Pipeline script from SCM" chỉ khi repo đã có trên Git; khi đó Jenkins checkout code từ Git rồi chạy file `Jenkinsfile` trong repo.

---

## Chạy SonarQube

### 1. Chạy SonarQube (Docker)

```bash
docker run -d --name sonarqube -p 9000:9000 sonarqube:latest
```

Đăng nhập: `http://localhost:9000` (admin / admin, đổi password lần đầu).

### 2. Tạo project trên SonarQube

- Login → Create project manually → chọn “Locally”
- Project key: `sonarqube-demo` (trùng với `sonar.projectKey` trong `sonar-project.properties`)

### 3. Scan với Maven

```bash
mvn clean verify sonar:sonar \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=<YOUR_SONAR_TOKEN>
```

Token tạo tại: **My Account → Security → Generate Token**.

### 4. Hoặc dùng SonarScanner CLI

Sau khi build:

```bash
mvn clean compile
sonar-scanner \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.token=<YOUR_SONAR_TOKEN>
```

## Kỳ vọng khi demo

- **Bugs**: NPE, resource leak, empty catch.
- **Vulnerabilities**: SQL injection, hardcoded secret.
- **Code smells**: magic numbers, duplication, cognitive complexity, raw types, `System.out`/`printStackTrace`.

So sánh số issue giữa package **good** (ít hoặc không) và **bad** (nhiều) để thấy tác dụng của SonarQube.

## API nhanh

```bash
# Tạo product
curl -X POST http://localhost:8080/api/products \
  -H "Content-Type: application/json" \
  -d '{"name":"Demo","price":99.99,"description":"Test"}'

# Lấy danh sách
curl http://localhost:8080/api/products
```

## License

MIT – dùng cho mục đích demo/ học tập.
