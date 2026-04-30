#!/bin/sh
# load-rdf.sh — Load all RDF files into Fuseki on first startup
# This script waits for Fuseki to be ready, then uploads RDF data.

FUSEKI_URL="${FUSEKI_URL:-http://fuseki:3030}"
DATASET="Products"
MARKER_FILE="/fuseki/databases/.data-loaded"
ADMIN_USER="admin"
ADMIN_PASS="${ADMIN_PASSWORD:-admin123}"

# Wait for Fuseki to be ready
echo "Waiting for Fuseki at $FUSEKI_URL ..."
until curl -sf -u "$ADMIN_USER:$ADMIN_PASS" "$FUSEKI_URL/\$/datasets" | grep -q "$DATASET"; do
    sleep 2
done
echo "Fuseki is ready!"

# Skip if data was already loaded
if [ -f "$MARKER_FILE" ]; then
    echo "Data already loaded (marker file exists). Skipping."
    exit 0
fi

# Load each RDF file
RDF_DIR="/rdf-data"
LOADED=0

for rdf_file in "$RDF_DIR"/*.rdf; do
    [ -f "$rdf_file" ] || continue
    filename=$(basename "$rdf_file")
    echo "Loading $filename into $DATASET ..."
    
    HTTP_CODE=$(curl -s -o /dev/null -w "%{http_code}" \
        -u "$ADMIN_USER:$ADMIN_PASS" \
        -X POST \
        -H "Content-Type: application/rdf+xml" \
        --data-binary "@$rdf_file" \
        "$FUSEKI_URL/$DATASET/data?graph=urn:file:$filename")
    
    if [ "$HTTP_CODE" -ge 200 ] && [ "$HTTP_CODE" -lt 300 ]; then
        echo "  OK: $filename loaded successfully (HTTP $HTTP_CODE)"
        LOADED=$((LOADED + 1))
    else
        echo "  FAIL: Failed to load $filename (HTTP $HTTP_CODE)"
    fi
done

echo "Loaded $LOADED RDF files into $DATASET."

# Create marker so we don't re-load on next startup
touch "$MARKER_FILE"
echo "Data loading complete."
