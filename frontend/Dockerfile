# ========================================================
# STAGE 1: Build ứng dụng React bằng Node.js
# ========================================================
FROM node:24-alpine AS build

WORKDIR /app

# 1. Copy package.json và package-lock.json để tối ưu cache
COPY package*.json ./

# 2. Cài đặt các dependencies (npm ci nhanh và ổn định hơn npm install)
RUN npm ci

# 3. Copy toàn bộ mã nguồn vào container
COPY . .

# 4. Build sản phẩm ra thư mục dist (hoặc build)
RUN npx vite build

# ========================================================
# STAGE 2: Phục vụ Web bằng Nginx siêu nhẹ
# ========================================================
FROM nginx:alpine

# 1. Copy cấu hình Nginx tùy chỉnh vào container
COPY nginx.conf /etc/nginx/conf.d/default.conf

# 2. Copy sản phẩm đã build từ Stage 1 sang Nginx
# Đổi /app/dist thành /app/build nếu dự án của bạn dùng Create React App
COPY --from=build /app/dist /usr/share/nginx/html

# Mở port 5173 cho Web
EXPOSE 5173

# Chạy Nginx ở chế độ foreground
CMD ["nginx", "-g", "daemon off;"]