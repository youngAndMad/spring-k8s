param (
    [string]$FolderName
)

Write-Output "Building and pushing Docker image for $FolderName"

if (-not $FolderName) {
    Write-Error "FolderName parameter is required."
    exit 1
}

# Change directory to the specified folder
try {
    Set-Location -Path $FolderName
} catch {
    Write-Error "Failed to change directory to $FolderName. Ensure the folder exists."
    exit 1
}

# Define the image name
$imageName = "daneker/spring-k8s-$FolderName"

# Verify image name format
if (-not $imageName) {
    Write-Error "Invalid image name."
    exit 1
}

Write-Output "Docker image name: $imageName"

# Build the Docker image
docker build -t $imageName .

# Check if the build was successful
if ($LASTEXITCODE -ne 0) {
    Write-Error "Docker build failed."
    exit $LASTEXITCODE
}

# Push the Docker image
docker push $imageName

# Check if the push was successful
if ($LASTEXITCODE -ne 0) {
    Write-Error "Docker push failed."
    exit $LASTEXITCODE
}

Write-Output "Docker image $imageName built and pushed successfully."
