FROM node:18.10-alpine AS build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:1.17.1-alpine
COPY --from=build /app/dist/locparc-angular /usr/share/nginx/html
COPY ./nginx-custom.conf /etc/nginx/conf.d/default.conf