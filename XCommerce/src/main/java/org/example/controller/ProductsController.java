package org.example.controller;

import org.example.dto.ProductSearchRequest;
import org.example.service.FusekiClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Collections;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final FusekiClient fusekiClient;
    private final ObjectMapper objectMapper;

    public ProductsController(FusekiClient fusekiClient) {
        this.fusekiClient = fusekiClient;
        this.objectMapper = new ObjectMapper();
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @PostMapping(value = "/search", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> searchProducts(@RequestBody ProductSearchRequest request) {
        Map<String, Object> response = new HashMap<>();
        response.put("category", request.getCategory());
        response.put("subCategory", request.getSubCategory());
        response.put("attributes", request.getAttributes());

        // Minimal backend implementation:
        // If you're using Fuseki, you can build a SPARQL query from these filters.
        // For now, we return the received payload plus (optional) raw Fuseki output when configured.
        try {
            if (request.getCategory() != null && request.getSubCategory() != null) {
                String sparql = buildSparql(request);
                response.put("sparql", sparql);
                String fusekiRaw = fusekiClient.runSelectQuery(sparql);
                response.put("fusekiRaw", fusekiRaw);

                try {
                    JsonNode root = objectMapper.readTree(fusekiRaw);

                    List<String> columns = new ArrayList<>();
                    columns.add("productName");
                    columns.add("price");
                    columns.add("basePrice");
                    columns.add("hasOffer");
                    columns.add("bestDiscDec");
                    columns.add("debugOfferCount");
                    columns.add("debugOffer");
                    columns.add("debugOfferProduct");
                    columns.add("debugOfferProductName");
                    columns.add("debugDiscRaw");
                    columns.add("debugDiscStr");
                    columns.add("debugDiscDec");
                    columns.add("debugOfferStart");
                    columns.add("debugOfferEnd");
                    columns.add("debugBpStr");
                    columns.add("debugBp");
                    columns.add("debugDiscPct");
                    columns.add("debugDiscRemain");
                    columns.add("debugBpDt");
                    columns.add("debugDiscRemainDt");
                    columns.add("debugBpTimesRemain");
                    columns.add("debugBpTimesRemainBound");
                    columns.add("debugDiscountedPrice2");
                    columns.add("debugDiscountedPrice2Bound");
                    columns.add("debugDiscountedPrice");
                    columns.add("debugDiscountedPriceBound");
                    columns.add("debugHasOfferInt");
                    columns.add("debugFinalPrice");
                    columns.add("platform");

                    List<Map<String, String>> rows = new ArrayList<>();
                    JsonNode bindings = root.path("results").path("bindings");
                    if (bindings.isArray()) {
                        for (JsonNode binding : bindings) {
                            Map<String, String> row = new HashMap<>();
                            JsonNode nameNode = binding.path("productName").path("value");
                            JsonNode basePriceNode = binding.path("basePrice").path("value");
                            JsonNode hasOfferNode = binding.path("hasOffer").path("value");
                            JsonNode bestDiscDecNode = binding.path("bestDiscDec").path("value");
                            JsonNode debugOfferCountNode = binding.path("debugOfferCount").path("value");
                            JsonNode debugOfferNode = binding.path("debugOffer").path("value");
                            JsonNode debugOfferProductNode = binding.path("debugOfferProduct").path("value");
                            JsonNode debugOfferProductNameNode = binding.path("debugOfferProductName").path("value");
                            JsonNode debugDiscRawNode = binding.path("debugDiscRaw").path("value");
                            JsonNode debugDiscStrNode = binding.path("debugDiscStr").path("value");
                            JsonNode debugDiscDecNode = binding.path("debugDiscDec").path("value");
                            JsonNode debugOfferStartNode = binding.path("debugOfferStart").path("value");
                            JsonNode debugOfferEndNode = binding.path("debugOfferEnd").path("value");
                            JsonNode debugBpStrNode = binding.path("debugBpStr").path("value");
                            JsonNode debugBpNode = binding.path("debugBp").path("value");
                            JsonNode debugDiscPctNode = binding.path("debugDiscPct").path("value");
                            JsonNode debugDiscRemainNode = binding.path("debugDiscRemain").path("value");
                            JsonNode debugBpDtNode = binding.path("debugBpDt").path("value");
                            JsonNode debugDiscRemainDtNode = binding.path("debugDiscRemainDt").path("value");
                            JsonNode debugBpTimesRemainNode = binding.path("debugBpTimesRemain").path("value");
                            JsonNode debugBpTimesRemainBoundNode = binding.path("debugBpTimesRemainBound").path("value");
                            JsonNode debugDiscountedPrice2Node = binding.path("debugDiscountedPrice2").path("value");
                            JsonNode debugDiscountedPrice2BoundNode = binding.path("debugDiscountedPrice2Bound").path("value");
                            JsonNode debugDiscountedPriceNode = binding.path("debugDiscountedPrice").path("value");
                            JsonNode debugDiscountedPriceBoundNode = binding.path("debugDiscountedPriceBound").path("value");
                            JsonNode debugHasOfferIntNode = binding.path("debugHasOfferInt").path("value");
                            JsonNode debugFinalPriceNode = binding.path("debugFinalPrice").path("value");
                            JsonNode platformNode = binding.path("platform").path("value");
                            if (nameNode.isTextual()) {
                                row.put("productName", nameNode.asText());
                            }
                            if (basePriceNode.isTextual()) {
                                row.put("basePrice", basePriceNode.asText());
                            }
                            if (hasOfferNode.isTextual()) {
                                row.put("hasOffer", hasOfferNode.asText());
                            }
                            if (bestDiscDecNode.isTextual()) {
                                row.put("bestDiscDec", bestDiscDecNode.asText());
                            }
                            if (debugOfferCountNode.isTextual()) {
                                row.put("debugOfferCount", debugOfferCountNode.asText());
                            }
                            if (debugOfferNode.isTextual()) {
                                row.put("debugOffer", debugOfferNode.asText());
                            }
                            if (debugOfferProductNode.isTextual()) {
                                row.put("debugOfferProduct", debugOfferProductNode.asText());
                            }
                            if (debugOfferProductNameNode.isTextual()) {
                                row.put("debugOfferProductName", debugOfferProductNameNode.asText());
                            }
                            if (debugDiscRawNode.isTextual()) {
                                row.put("debugDiscRaw", debugDiscRawNode.asText());
                            }
                            if (debugDiscStrNode.isTextual()) {
                                row.put("debugDiscStr", debugDiscStrNode.asText());
                            }
                            if (debugDiscDecNode.isTextual()) {
                                row.put("debugDiscDec", debugDiscDecNode.asText());
                            }
                            if (debugOfferStartNode.isTextual()) {
                                row.put("debugOfferStart", debugOfferStartNode.asText());
                            }
                            if (debugOfferEndNode.isTextual()) {
                                row.put("debugOfferEnd", debugOfferEndNode.asText());
                            }
                            if (debugBpStrNode.isTextual()) {
                                row.put("debugBpStr", debugBpStrNode.asText());
                            }
                            if (debugBpNode.isTextual()) {
                                row.put("debugBp", debugBpNode.asText());
                            }
                            if (debugDiscPctNode.isTextual()) {
                                row.put("debugDiscPct", debugDiscPctNode.asText());
                            }
                            if (debugDiscRemainNode.isTextual()) {
                                row.put("debugDiscRemain", debugDiscRemainNode.asText());
                            }
                            if (debugBpDtNode.isTextual()) {
                                row.put("debugBpDt", debugBpDtNode.asText());
                            }
                            if (debugDiscRemainDtNode.isTextual()) {
                                row.put("debugDiscRemainDt", debugDiscRemainDtNode.asText());
                            }
                            if (debugBpTimesRemainNode.isTextual()) {
                                row.put("debugBpTimesRemain", debugBpTimesRemainNode.asText());
                            }
                            if (debugBpTimesRemainBoundNode.isTextual()) {
                                row.put("debugBpTimesRemainBound", debugBpTimesRemainBoundNode.asText());
                            }
                            if (debugDiscountedPrice2Node.isTextual()) {
                                row.put("debugDiscountedPrice2", debugDiscountedPrice2Node.asText());
                            }
                            if (debugDiscountedPrice2BoundNode.isTextual()) {
                                row.put("debugDiscountedPrice2Bound", debugDiscountedPrice2BoundNode.asText());
                            }
                            if (debugDiscountedPriceNode.isTextual()) {
                                row.put("debugDiscountedPrice", debugDiscountedPriceNode.asText());
                            }
                            if (debugDiscountedPriceBoundNode.isTextual()) {
                                row.put("debugDiscountedPriceBound", debugDiscountedPriceBoundNode.asText());
                            }
                            if (debugHasOfferIntNode.isTextual()) {
                                row.put("debugHasOfferInt", debugHasOfferIntNode.asText());
                            }
                            if (debugFinalPriceNode.isTextual()) {
                                row.put("debugFinalPrice", debugFinalPriceNode.asText());
                            }
                            if (platformNode.isTextual()) {
                                row.put("platform", platformNode.asText());
                            }

                            // Compute final price & hasOffer in Java to avoid Jena/ARQ arithmetic unbinding.
                            try {
                                String bpS = row.get("basePrice");
                                String discS = row.get("bestDiscDec");
                                if (bpS != null && !bpS.isBlank()) {
                                    double bp = Double.parseDouble(bpS);
                                    if (discS != null && !discS.isBlank()) {
                                        double disc = Double.parseDouble(discS);
                                        if (disc > 0) {
                                            double finalP = bp * (1.0 - (disc / 100.0));
                                            row.put("price", String.valueOf(finalP));
                                            row.put("hasOffer", "1");
                                        } else {
                                            row.put("price", String.valueOf(bp));
                                            row.put("hasOffer", "0");
                                        }
                                    } else {
                                        row.put("price", String.valueOf(bp));
                                        row.put("hasOffer", "0");
                                    }
                                }
                            } catch (Exception ignored) {
                                // leave as-is
                            }
                            rows.add(row);
                        }
                    }

                    response.put("columns", columns);
                    response.put("rows", rows);
                } catch (Exception ignored) {
                    // If Fuseki returns non-JSON or unexpected shape, keep fusekiRaw only.
                }
            }
        } catch (Exception e) {
            response.put("fusekiError", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    @CrossOrigin(origins = {"http://localhost:8080", "http://127.0.0.1:8080"})
    @PostMapping(value = "/filter-options", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getFilterOptions(@RequestBody ProductSearchRequest request) {
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("category", request.getCategory());
        response.put("subCategory", request.getSubCategory());

        String typeIri = mapUnifiedTypeIri(request.getCategory(), request.getSubCategory());

        try {
            Map<String, List<String>> options = new LinkedHashMap<>();

            if ("Furniture".equalsIgnoreCase(String.valueOf(request.getCategory()))) {
                options.put("Brand", queryDistinctLiteralValues(typeIri, "x:brand"));
                options.put("Color", queryDistinctLiteralValues(typeIri, "x:color"));
                options.put("Material", queryDistinctLiteralValues(typeIri, "x:material"));
                options.put("Dimensions", queryDistinctLiteralValues(typeIri, "x:dimensions"));
                options.put("AssemblyRequired", queryDistinctLiteralValues(typeIri, "x:isAssemblyRequired"));
                options.put("Seaters", queryDistinctLiteralValues(typeIri, "x:seaters"));

                options.put("HasArmRest", queryDistinctLiteralValues(typeIri, "x:hasarmRest"));
                options.put("HasWheels", queryDistinctLiteralValues(typeIri, "x:hasWheels"));
                options.put("IsRecliner", queryDistinctLiteralValues(typeIri, "x:isRecliner"));
                options.put("UpholsteryType", queryDistinctLiteralValues(typeIri, "x:upholsteryType"));
                options.put("HasAdjustableHeight", queryDistinctLiteralValues(typeIri, "x:hasAdjustableHeight"));
                options.put("IsRotating", queryDistinctLiteralValues(typeIri, "x:isRotating"));
                options.put("LegCount", queryDistinctLiteralValues(typeIri, "x:legCount"));
                options.put("SeatingCapacity", queryDistinctLiteralValues(typeIri, "x:seatingCapacity"));
                options.put("Shape", queryDistinctLiteralValues(typeIri, "x:shape"));
                options.put("IsConvertible", queryDistinctLiteralValues(typeIri, "x:isConvertible"));

                options.put("ShelfCount", queryDistinctLiteralValues(typeIri, "x:shelfCount"));
                options.put("Volume", queryDistinctLiteralValues(typeIri, "x:volume"));
                options.put("DoorCount", queryDistinctLiteralValues(typeIri, "x:doorCount"));
                options.put("DrawerCount", queryDistinctLiteralValues(typeIri, "x:drawerCount"));
                options.put("HasLock", queryDistinctLiteralValues(typeIri, "x:hasLock"));
                options.put("IsOpen", queryDistinctLiteralValues(typeIri, "x:isOpen"));
                options.put("MountType", queryDistinctLiteralValues(typeIri, "x:mountType"));
                options.put("DoorKind", queryDistinctLiteralValues(typeIri, "x:doorKind"));
                options.put("HasLocker", queryDistinctLiteralValues(typeIri, "x:hasLocker"));
                options.put("HasMirror", queryDistinctLiteralValues(typeIri, "x:hasMirror"));

                options.put("WeightCap", queryDistinctLiteralValues(typeIri, "x:weightCap"));
                options.put("TableTopThickness", queryDistinctLiteralValues(typeIri, "x:tableTopThickness"));
                options.put("TopMaterial", queryDistinctLiteralValues(typeIri, "x:topMaterial"));
                options.put("HasStorage", queryDistinctLiteralValues(typeIri, "x:hasStorage"));
            } else {
                options.put("Brand", queryDistinctLiteralValues(typeIri, "x:brand"));
                options.put("Color", queryDistinctLiteralValues(typeIri, "x:color"));
                options.put("StorageValue", queryDistinctLiteralValues(typeIri, "x:storageValue"));
                options.put("StorageUnit", queryDistinctLiteralValues(typeIri, "x:storageUnit"));
                options.put("HDDValue", queryDistinctLiteralValues(typeIri, "x:hddValue"));
                options.put("HDDUnit", queryDistinctLiteralValues(typeIri, "x:hddUnit"));
                options.put("RAMValue", queryDistinctLiteralValues(typeIri, "x:ramValue"));
                options.put("RAMUnit", queryDistinctLiteralValues(typeIri, "x:ramUnit"));
                options.put("OS", queryDistinctLiteralValues(typeIri, "x:os"));
                options.put("DisplayTechnology", queryDistinctLiteralValues(typeIri, "x:displayTech"));
                options.put("DisplaySize", queryDistinctLiteralValues(typeIri, "x:displaySize"));
                options.put("ProcessorSpeedValue", queryDistinctLiteralValues(typeIri, "x:procSpeedValue"));
                options.put("ProcessorSpeedUnit", queryDistinctLiteralValues(typeIri, "x:procSpeedUnit"));
                options.put("ManufacturingYear", queryDistinctLiteralValues(typeIri, "x:manufacturingYear"));
                options.put("BatteryLife", queryDistinctLiteralValues(typeIri, "x:batteryLife"));
                options.put("Pixels", queryDistinctLiteralValues(typeIri, "x:camRes"));
                options.put("SIMSlots", queryDistinctLiteralValues(typeIri, "x:simCardSlotCnt"));
                options.put("ScreenResolution", queryDistinctLiteralValues(typeIri, "x:resolution"));
            }

            response.put("options", options);
        } catch (Exception e) {
            response.put("fusekiError", e.getMessage());
        }

        return ResponseEntity.ok(response);
    }

    private String buildSparql(ProductSearchRequest request) {
        // NOTE: Update these mappings if your ontology uses different predicate names.
        final String prefix = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX xsd: <http://www.w3.org/2001/XMLSchema#>\n" +
                "PREFIX x: <http://dm-project.org/multi-portal-xcommerce#>\n";

        String typeIri = mapUnifiedTypeIri(request.getCategory(), request.getSubCategory());
        String namePredicate = "x:productName";

        StringBuilder where = new StringBuilder();
        if (typeIri != null) {
            // Match resources typed with a class equivalent to the requested unified class.
            // (We cannot assume Fuseki inference is enabled.)
            where.append("  ?product rdf:type ?t .\n");
            where.append("  ?t (owl:equivalentClass|^owl:equivalentClass)* ").append(typeIri).append(" .\n");
            // Also allow subclasses of equivalent classes.
            where.append("  ?t rdfs:subClassOf* ?t2 .\n");
            where.append("  ?t2 (owl:equivalentClass|^owl:equivalentClass)* ").append(typeIri).append(" .\n");
        }
        // Fetch values using any predicate that is equivalent to the unified predicate.
        appendEquivalentRequiredValue(where, "productName", namePredicate);
        appendEquivalentRequiredValue(where, "basePriceLit", "x:price");
        appendEquivalentOptionalValue(where, "platformV", "x:platform");

        where.append("  BIND(REPLACE(STR(?basePriceLit), \"[^0-9.]\", \"\") AS ?bpStr) .\n");
        where.append("  FILTER(?bpStr != \"\") .\n");
        where.append("  BIND(xsd:decimal(?bpStr) AS ?bp) .\n");
        where.append("  OPTIONAL {\n");
        where.append("    ?pOnProduct (owl:equivalentProperty|^owl:equivalentProperty)* x:onProduct .\n");
        where.append("    ?offer ?pOnProduct ?offerProduct .\n");
        where.append("    OPTIONAL {\n");
        where.append("      ?pOfferProdName (owl:equivalentProperty|^owl:equivalentProperty)* x:productName .\n");
        where.append("      ?offerProduct ?pOfferProdName ?offerProductName .\n");
        where.append("    }\n");
        where.append("    FILTER( !BOUND(?offerProductName) || LCASE(STR(?offerProductName)) = LCASE(STR(?productName)) )\n");
        where.append("    ?pDiscount (owl:equivalentProperty|^owl:equivalentProperty)* x:offeredDiscount .\n");
        where.append("    ?offer ?pDiscount ?disc .\n");
        where.append("    OPTIONAL {\n");
        where.append("      ?pOfferStart (owl:equivalentProperty|^owl:equivalentProperty)* x:offerStartDate .\n");
        where.append("      ?offer ?pOfferStart ?offerStart .\n");
        where.append("    }\n");
        where.append("    OPTIONAL {\n");
        where.append("      ?pOfferEnd (owl:equivalentProperty|^owl:equivalentProperty)* x:offerEndDate .\n");
        where.append("      ?offer ?pOfferEnd ?offerEnd .\n");
        where.append("    }\n");
        where.append("    FILTER(\n");
        where.append("      ( !BOUND(?offerStart) || DATATYPE(?offerStart) != xsd:dateTime || ?offerStart <= NOW() ) &&\n");
        where.append("      ( !BOUND(?offerEnd) || DATATYPE(?offerEnd) != xsd:dateTime || ?offerEnd >= NOW() )\n");
        where.append("    )\n");
        where.append("    BIND(REPLACE(STR(?disc), \"[^0-9.]\", \"\") AS ?discStr) .\n");
        where.append("    FILTER(?discStr != \"\") .\n");
        where.append("    BIND(xsd:decimal(?discStr) AS ?discDec) .\n");
        where.append("  }\n");

        // Keep these for compatibility with existing debug projection.
        where.append("  BIND(?discDec AS ?discountedPrice) .\n");
        where.append("  BIND(xsd:decimal(0) AS ?finalPrice) .\n");
        where.append("  BIND(0 AS ?hasOfferInt) .\n");

        Map<String, String> attrs = request.getAttributes();
        if (attrs != null) {
            if ("Furniture".equalsIgnoreCase(String.valueOf(request.getCategory()))) {
                appendLiteralFilter(where, "Brand", "x:brand", attrs.get("Brand"));
                appendLiteralFilter(where, "Color", "x:color", attrs.get("Color"));
                appendLiteralFilter(where, "Material", "x:material", attrs.get("Material"));
                appendLiteralFilter(where, "Dimensions", "x:dimensions", attrs.get("Dimensions"));
                appendLiteralFilter(where, "AssemblyRequired", "x:isAssemblyRequired", attrs.get("AssemblyRequired"));
                appendLiteralFilter(where, "Seaters", "x:seaters", attrs.get("Seaters"));

                appendLiteralFilter(where, "HasArmRest", "x:hasarmRest", attrs.get("HasArmRest"));
                appendLiteralFilter(where, "HasWheels", "x:hasWheels", attrs.get("HasWheels"));
                appendLiteralFilter(where, "IsRecliner", "x:isRecliner", attrs.get("IsRecliner"));
                appendLiteralFilter(where, "UpholsteryType", "x:upholsteryType", attrs.get("UpholsteryType"));
                appendLiteralFilter(where, "HasAdjustableHeight", "x:hasAdjustableHeight", attrs.get("HasAdjustableHeight"));
                appendLiteralFilter(where, "IsRotating", "x:isRotating", attrs.get("IsRotating"));
                appendLiteralFilter(where, "LegCount", "x:legCount", attrs.get("LegCount"));
                appendLiteralFilter(where, "SeatingCapacity", "x:seatingCapacity", attrs.get("SeatingCapacity"));
                appendLiteralFilter(where, "Shape", "x:shape", attrs.get("Shape"));
                appendLiteralFilter(where, "IsConvertible", "x:isConvertible", attrs.get("IsConvertible"));

                appendLiteralFilter(where, "ShelfCount", "x:shelfCount", attrs.get("ShelfCount"));
                appendLiteralFilter(where, "Volume", "x:volume", attrs.get("Volume"));
                appendLiteralFilter(where, "DoorCount", "x:doorCount", attrs.get("DoorCount"));
                appendLiteralFilter(where, "DrawerCount", "x:drawerCount", attrs.get("DrawerCount"));
                appendLiteralFilter(where, "HasLock", "x:hasLock", attrs.get("HasLock"));
                appendLiteralFilter(where, "IsOpen", "x:isOpen", attrs.get("IsOpen"));
                appendLiteralFilter(where, "MountType", "x:mountType", attrs.get("MountType"));
                appendLiteralFilter(where, "DoorKind", "x:doorKind", attrs.get("DoorKind"));
                appendLiteralFilter(where, "HasLocker", "x:hasLocker", attrs.get("HasLocker"));
                appendLiteralFilter(where, "HasMirror", "x:hasMirror", attrs.get("HasMirror"));

                appendLiteralFilter(where, "WeightCap", "x:weightCap", attrs.get("WeightCap"));
                appendLiteralFilter(where, "TableTopThickness", "x:tableTopThickness", attrs.get("TableTopThickness"));
                appendLiteralFilter(where, "TopMaterial", "x:topMaterial", attrs.get("TopMaterial"));
                appendLiteralFilter(where, "HasStorage", "x:hasStorage", attrs.get("HasStorage"));
            } else {
                appendLiteralFilter(where, "Brand", "x:brand", attrs.get("Brand"));
                if ("Laptop".equalsIgnoreCase(request.getSubCategory())) {
                    appendHddFilter(where, attrs.get("HDD"));
                } else {
                    appendStorageFilter(where, attrs.get("Storage"));
                }
                appendLiteralFilter(where, "Color", "x:color", attrs.get("Color"));
                appendRamFilter(where, attrs.get("RAM"));
                appendLiteralFilter(where, "OS", "x:os", attrs.get("OS"));
                appendLiteralFilter(where, "DisplayTechnology", "x:displayTech", attrs.get("DisplayTechnology"));
                appendLiteralFilter(where, "DisplaySize", "x:displaySize", attrs.get("DisplaySize"));
                appendProcSpeedFilter(where, attrs.get("ProcessorSpeed"));
                appendLiteralFilter(where, "ManufacturingYear", "x:manufacturingYear", attrs.get("ManufacturingYear"));
                appendLiteralFilter(where, "BatteryLife", "x:batteryLife", attrs.get("BatteryLife"));
                appendLiteralFilter(where, "Pixels", "x:camRes", attrs.get("Pixels"));
                appendLiteralFilter(where, "SIMSlots", "x:simCardSlotCnt", attrs.get("SIMSlots"));
                appendLiteralFilter(where, "ScreenResolution", "x:resolution", attrs.get("ScreenResolution"));
            }
            // NOTE: ProcessorSpeed and other UI filters are not mapped yet because the unified ontology
            // predicates weren't provided. We can wire them once you share those IRIs.
        }

        return prefix +
                "SELECT ?productName " +
                "(SAMPLE(?bp) AS ?basePrice) " +
                "(MAX(?discDec) AS ?bestDiscDec) " +
                "(MAX(IF(BOUND(?discDec), 1, 0)) AS ?hasOffer) " +
                "(COUNT(?offer) AS ?debugOfferCount) " +
                "(SAMPLE(STR(?offer)) AS ?debugOffer) " +
                "(SAMPLE(STR(?offerProduct)) AS ?debugOfferProduct) " +
                "(SAMPLE(STR(?offerProductName)) AS ?debugOfferProductName) " +
                "(SAMPLE(STR(?disc)) AS ?debugDiscRaw) " +
                "(SAMPLE(STR(?discStr)) AS ?debugDiscStr) " +
                "(SAMPLE(STR(?discDec)) AS ?debugDiscDec) " +
                "(SAMPLE(STR(?bp)) AS ?debugBp) " +
                "(SAMPLE(STR(?offerStart)) AS ?debugOfferStart) " +
                "(SAMPLE(STR(?offerEnd)) AS ?debugOfferEnd) " +
                "(SAMPLE(STR(?bpStr)) AS ?debugBpStr) " +
                "(SAMPLE(?platformV) AS ?platform) WHERE {\n" +
                "  GRAPH <urn:x-arq:UnionGraph> {\n" +
                where +
                "  }\n" +
                "}\n" +
                "GROUP BY ?product ?productName\n" +
                "ORDER BY ?productName\n";
    }

    private String mapUnifiedTypeIri(String category, String subCategory) {
        if (subCategory == null) {
            return null;
        }
        String sc = subCategory.trim();

        if ("Furniture".equalsIgnoreCase(String.valueOf(category))) {
            if (sc.equalsIgnoreCase("Chair")) {
                return "x:Chair";
            }
            if (sc.equalsIgnoreCase("Sofa")) {
                return "x:Sofa";
            }
            if (sc.equalsIgnoreCase("Shelf")) {
                return "x:Shelf";
            }
            if (sc.equalsIgnoreCase("Wardrobe")) {
                return "x:Wardrobe";
            }
            if (sc.equalsIgnoreCase("Dining")) {
                return "x:Dining";
            }
            if (sc.equalsIgnoreCase("Study")) {
                return "x:Study";
            }
            return null;
        }

        if (sc.equalsIgnoreCase("Laptop")) {
            return "x:Laptop";
        }
        if (sc.equalsIgnoreCase("Mobile")) {
            return "x:Mobile";
        }
        if (sc.equalsIgnoreCase("TV") || sc.equalsIgnoreCase("Television")) {
            return "x:Television";
        }
        return null;
    }

    private String buildTypeWhere(String typeIri) {
        if (typeIri == null || typeIri.isBlank()) {
            return "";
        }

        StringBuilder where = new StringBuilder();
        where.append("  ?product rdf:type ?t .\n");
        where.append("  ?t (owl:equivalentClass|^owl:equivalentClass)* ").append(typeIri).append(" .\n");
        where.append("  ?t rdfs:subClassOf* ?t2 .\n");
        where.append("  ?t2 (owl:equivalentClass|^owl:equivalentClass)* ").append(typeIri).append(" .\n");
        return where.toString();
    }

    private List<String> queryDistinctLiteralValues(String typeIri, String unifiedPredicate) {
        final String prefix = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\n" +
                "PREFIX rdfs: <http://www.w3.org/2000/01/rdf-schema#>\n" +
                "PREFIX owl: <http://www.w3.org/2002/07/owl#>\n" +
                "PREFIX x: <http://dm-project.org/multi-portal-xcommerce#>\n";

        StringBuilder where = new StringBuilder();
        where.append(buildTypeWhere(typeIri));
        where.append("  ?p (owl:equivalentProperty|^owl:equivalentProperty)* ").append(unifiedPredicate).append(" .\n");
        where.append("  ?product ?p ?v .\n");
        where.append("  FILTER(isLiteral(?v)) .\n");

        String sparql = prefix +
                "SELECT DISTINCT (STR(?v) as ?value) WHERE {\n" +
                "  GRAPH <urn:x-arq:UnionGraph> {\n" +
                where +
                "  }\n" +
                "}\n" +
                "ORDER BY LCASE(STR(?value))\n";

        String fusekiRaw = fusekiClient.runSelectQuery(sparql);
        List<String> values = new ArrayList<>();

        try {
            JsonNode root = objectMapper.readTree(fusekiRaw);
            JsonNode bindings = root.path("results").path("bindings");
            if (bindings.isArray()) {
                for (JsonNode binding : bindings) {
                    JsonNode vNode = binding.path("value").path("value");
                    if (vNode.isTextual()) {
                        String s = vNode.asText();
                        if (s != null) {
                            String trimmed = s.trim();
                            if (!trimmed.isEmpty()) {
                                values.add(trimmed);
                            }
                        }
                    }
                }
            }
        } catch (Exception ignored) {
        }

        if (values.isEmpty()) {
            return values;
        }

        Collections.sort(values, String.CASE_INSENSITIVE_ORDER);
        List<String> distinct = new ArrayList<>();
        String prev = null;
        for (String v : values) {
            if (prev == null || !prev.equalsIgnoreCase(v)) {
                distinct.add(v);
                prev = v;
            }
        }
        return distinct;
    }

    private void appendLiteralFilter(StringBuilder where, String attributeKey, String predicate, String value) {
        if (value == null || value.isBlank()) {
            return;
        }
        String var = attributeKey.replaceAll("[^A-Za-z0-9]", "");
        String predVar = "p" + var;
        where.append("  ?").append(predVar).append(" (owl:equivalentProperty|^owl:equivalentProperty)* ").append(predicate).append(" .\n");
        where.append("  ?product ?").append(predVar).append(" ?").append(var).append(" .\n");
        String escaped = value.replace("\\", "\\\\").replace("\"", "\\\"");
        where.append("  FILTER(LCASE(STR(?").append(var).append(")) = LCASE(\"").append(escaped).append("\")) .\n");
    }

    private void appendStorageFilter(StringBuilder where, String value) {
        if (value == null || value.isBlank()) {
            return;
        }

        // Unified ontology stores storage as separate value+unit.
        // UI may send values like "64GB", "128GB", "512GB SSD".
        // We'll try to parse a leading integer and use it as storageValue; unit is matched loosely.
        String trimmed = value.trim();
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("^(\\d+)").matcher(trimmed);

        where.append("  ?pStorageValue (owl:equivalentProperty|^owl:equivalentProperty)* x:storageValue .\n");
        where.append("  ?pStorageUnit (owl:equivalentProperty|^owl:equivalentProperty)* x:storageUnit .\n");
        where.append("  ?product ?pStorageValue ?storageValue .\n");
        where.append("  ?product ?pStorageUnit ?storageUnit .\n");

        if (m.find()) {
            String num = m.group(1);
            where.append("  FILTER(?storageValue = ").append(num).append(") .\n");
        }

        String escaped = trimmed.replace("\\", "\\\\").replace("\"", "\\\"");
        where.append("  FILTER(CONTAINS(LCASE(STR(?storageUnit)), LCASE(\"").append(escaped).append("\")) || CONTAINS(LCASE(\"").append(escaped).append("\"), LCASE(STR(?storageUnit)))) .\n");
    }

    private void appendHddFilter(StringBuilder where, String value) {
        if (value == null || value.isBlank()) {
            return;
        }

        String trimmed = value.trim();
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("^(\\d+)").matcher(trimmed);

        where.append("  ?pHddValue (owl:equivalentProperty|^owl:equivalentProperty)* x:hddValue .\n");
        where.append("  ?pHddUnit (owl:equivalentProperty|^owl:equivalentProperty)* x:hddUnit .\n");
        where.append("  ?product ?pHddValue ?hddValue .\n");
        where.append("  ?product ?pHddUnit ?hddUnit .\n");

        if (m.find()) {
            String num = m.group(1);
            where.append("  FILTER(?hddValue = ").append(num).append(") .\n");
        }

        String escaped = trimmed.replace("\\", "\\\\").replace("\"", "\\\"");
        where.append("  FILTER(CONTAINS(LCASE(STR(?hddUnit)), LCASE(\"").append(escaped).append("\")) || CONTAINS(LCASE(\"").append(escaped).append("\"), LCASE(STR(?hddUnit)))) .\n");
    }

    private void appendRamFilter(StringBuilder where, String value) {
        if (value == null || value.isBlank()) {
            return;
        }

        String trimmed = value.trim();
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("^(\\d+)").matcher(trimmed);

        where.append("  ?pRamValue (owl:equivalentProperty|^owl:equivalentProperty)* x:ramValue .\n");
        where.append("  ?pRamUnit (owl:equivalentProperty|^owl:equivalentProperty)* x:ramUnit .\n");
        where.append("  ?product ?pRamValue ?ramValue .\n");
        where.append("  ?product ?pRamUnit ?ramUnit .\n");

        if (m.find()) {
            String num = m.group(1);
            where.append("  FILTER(?ramValue = ").append(num).append(") .\n");
        }

        String escaped = trimmed.replace("\\", "\\\\").replace("\"", "\\\"");
        where.append("  FILTER(CONTAINS(LCASE(STR(?ramUnit)), LCASE(\"").append(escaped).append("\")) || CONTAINS(LCASE(\"").append(escaped).append("\"), LCASE(STR(?ramUnit)))) .\n");
    }

    private void appendProcSpeedFilter(StringBuilder where, String value) {
        if (value == null || value.isBlank()) {
            return;
        }

        String trimmed = value.trim();
        java.util.regex.Matcher m = java.util.regex.Pattern.compile("^(\\d+(?:\\.\\d+)?)").matcher(trimmed);

        where.append("  ?pProcSpeedValue (owl:equivalentProperty|^owl:equivalentProperty)* x:procSpeedValue .\n");
        where.append("  ?pProcSpeedUnit (owl:equivalentProperty|^owl:equivalentProperty)* x:procSpeedUnit .\n");
        where.append("  ?product ?pProcSpeedValue ?procSpeedValue .\n");
        where.append("  ?product ?pProcSpeedUnit ?procSpeedUnit .\n");

        if (m.find()) {
            String num = m.group(1);
            where.append("  FILTER(?procSpeedValue = ").append(num).append(") .\n");
        }

        String escaped = trimmed.replace("\\", "\\\\").replace("\"", "\\\"");
        where.append("  FILTER(CONTAINS(LCASE(STR(?procSpeedUnit)), LCASE(\"").append(escaped).append("\")) || CONTAINS(LCASE(\"").append(escaped).append("\"), LCASE(STR(?procSpeedUnit)))) .\n");
    }

    private void appendEquivalentRequiredValue(StringBuilder where, String varName, String unifiedPredicate) {
        where.append("  ?p").append(varName).append(" (owl:equivalentProperty|^owl:equivalentProperty)* ")
                .append(unifiedPredicate).append(" .\n");
        where.append("  ?product ?p").append(varName).append(" ?").append(varName).append(" .\n");
    }

    private void appendEquivalentOptionalValue(StringBuilder where, String varName, String unifiedPredicate) {
        where.append("  OPTIONAL {\n");
        where.append("    ?p").append(varName).append(" (owl:equivalentProperty|^owl:equivalentProperty)* ")
                .append(unifiedPredicate).append(" .\n");
        where.append("    ?product ?p").append(varName).append(" ?").append(varName).append(" .\n");
        where.append("  }\n");
    }
}
