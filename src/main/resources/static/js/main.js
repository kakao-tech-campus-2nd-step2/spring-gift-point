async function requestApi(targetUrl, headers, body, method) {
  return fetch(targetUrl, {
    method: method,
    headers: headers,
    body: body,
  })
  .then((response) => response.json())
  .catch((error) => {
    throw error;
  });
}

function requestView(targetUrl, headers) {
  fetch(targetUrl, {
    method: 'get',
    headers: headers,
  })
  .then((response) => response.text())
  .then((html) => {
    history.pushState(null, '', targetUrl);
    document.open();
    document.write(html);
    document.close();
  })
  .catch((error) => console.error('Error:', error));
}
