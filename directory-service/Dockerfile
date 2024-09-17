# Build stage
FROM rust:1.80-bullseye AS builder

WORKDIR /app

# Copy our manifests
COPY ./Cargo.lock ./Cargo.lock
COPY ./Cargo.toml ./Cargo.toml

# Copy the source code
COPY ./src ./src

# Build for release
RUN cargo build --release

# Final stage
FROM debian:bullseye-slim

# Copy the build artifact from the build stage
COPY --from=builder /app/target/release/directory-service .

# Set the startup command to run your binary
CMD ["./directory-service"]