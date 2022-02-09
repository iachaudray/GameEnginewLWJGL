#type vertex
#version 410
layout (location = 0) in vec3 aPos;

uniform mat4 projectionMatrix;
uniform mat4 viewMatrix;
uniform mat4 modelMatrix;
uniform mat3 normalMatrix;
out vec3 pos;
out vec3 geoPos;
void main() {
    pos = aPos;
    gl_Position = (projectionMatrix * viewMatrix * modelMatrix) *   vec4(aPos, 1.0);
    geoPos =  normalMatrix * aPos;

}

#type fragment
#version 410
out vec4 color;
uniform vec3 objectColor;
uniform vec3 lightColor;
uniform vec3 lightPos;
uniform vec3 cameraPos;
uniform vec3 sunLightDirection;
in vec3 N;
in vec3 fragPos;
void main() {
    //calculate diffusion light
    /*vec3 lightDir = normalize(lightPos - fragPos);
    float diff = max(dot(N, lightDir), 0.0);
    vec3 diffuse = diff * lightColor;

    //calculate specular light
    float specularStrength = 0.5;
    vec3 viewDir = normalize(cameraPos - fragPos);
    vec3 reflectDir = reflect(-lightDir, N);
    float spec = pow(max(dot(viewDir, reflectDir), 0.0), 16);
    //vec3 distanceVector = cameraPos - fragPos;
    //float dist = sqrt(sqrt(dot(distanceVector, distanceVector)));
    vec3 specular = specularStrength * spec * lightColor;

    // calculate ambient light

    vec3 ambient = ambientStrength * objectColor;
    // calculate everything together
    vec3 result = (ambient + specular + diffuse) * objectColor;
    */
    float ambientStrength = 0.7;
    float x = max(dot(N, sunLightDirection), 0);
    vec3 light = (ambientStrength + x) * objectColor;
    color = vec4(light, 1.0);

}

#type geometry
#version 330 core
layout (triangles) in;
layout (triangle_strip, max_vertices = 6) out;
in vec3 pos[];
in vec3 geoPos[];
out vec3 N;
out vec3 fragPos;
void main() {

    vec3 a = ( pos[1] - pos[0] );
    vec3 b = ( pos[2] - pos[0] );
    N = normalize( cross( b, a ) );
    gl_Position = gl_in[0].gl_Position;
    fragPos = geoPos[0];
    EmitVertex();
    gl_Position = gl_in[1].gl_Position;
    fragPos = geoPos[1];
    EmitVertex();
    gl_Position = gl_in[2].gl_Position;
    fragPos = geoPos[2];
    EmitVertex();
    EndPrimitive();
}